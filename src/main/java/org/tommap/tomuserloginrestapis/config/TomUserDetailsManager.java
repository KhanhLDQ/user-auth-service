package org.tommap.tomuserloginrestapis.config;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.tommap.tomuserloginrestapis.repository.UserRepository;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class TomUserDetailsManager implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User %s not found", username)));

        Set<SimpleGrantedAuthority> grantedAuthorities = user.getRoles().stream()
                .flatMap(role -> Stream.concat(
                            Stream.of(new SimpleGrantedAuthority(role.getName())),
                            Optional.ofNullable(role.getAuthorities())
                                    .orElse(Collections.emptySet())
                                    .stream()
                                    .map(authority -> new SimpleGrantedAuthority(authority.getName()))
                        )
                )
                .collect(Collectors.toUnmodifiableSet());

        return new User(username, user.getEncryptedPassword(), grantedAuthorities);
    }
}
