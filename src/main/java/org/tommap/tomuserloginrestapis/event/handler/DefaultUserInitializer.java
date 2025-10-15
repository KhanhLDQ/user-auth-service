package org.tommap.tomuserloginrestapis.event.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.tommap.tomuserloginrestapis.exception.ResourceNotFoundException;
import org.tommap.tomuserloginrestapis.model.entity.Role;
import org.tommap.tomuserloginrestapis.model.entity.User;
import org.tommap.tomuserloginrestapis.repository.RoleRepository;
import org.tommap.tomuserloginrestapis.repository.UserRepository;
import org.tommap.tomuserloginrestapis.shared.UserUtils;

import java.util.Optional;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class DefaultUserInitializer {
    private static final String ADMIN_EMAIL = "admin@gmail.com";
    private static final String ADMIN_PASSWORD = "Admin123@A";

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserUtils userUtils;
    private final PasswordEncoder passwordEncoder;

    @EventListener
    @Transactional
    public void init(ApplicationReadyEvent event) {
        Optional<User> userOptional = userRepository.findByEmail(ADMIN_EMAIL);

        if (userOptional.isPresent()) {
            log.debug("Admin User {} already existed, skip initialization", ADMIN_EMAIL);
            return;
        }

        log.info("Init Admin User: {}", ADMIN_EMAIL);

        Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                .orElseThrow(() -> new ResourceNotFoundException("ROLE ADMIN not found"));

        User adminUser = new User();
        adminUser.setUserId(userUtils.generateUserId());
        adminUser.setFirstName("admin");
        adminUser.setLastName("app");
        adminUser.setEmail(ADMIN_EMAIL);
        adminUser.setEncryptedPassword(passwordEncoder.encode(ADMIN_PASSWORD));
        adminUser.setEmailVerificationStatus(true);
        adminUser.setRoles(Set.of(adminRole));

        userRepository.save(adminUser);
    }
}
