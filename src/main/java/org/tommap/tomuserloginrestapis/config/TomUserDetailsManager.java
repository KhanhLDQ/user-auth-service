package org.tommap.tomuserloginrestapis.config;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.tommap.tomuserloginrestapis.repository.UserRepository;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TomUserDetailsManager implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException(String.format("User %s not found", username)));

        return new User(username, user.getEncryptedPassword(), List.of());
    }
}
