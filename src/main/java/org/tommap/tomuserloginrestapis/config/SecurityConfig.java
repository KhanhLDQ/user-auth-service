package org.tommap.tomuserloginrestapis.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.tommap.tomuserloginrestapis.exception.JwtAuthenticationEntryPoint;
import org.tommap.tomuserloginrestapis.filter.JwtAuthFilter;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(
            HttpSecurity httpSecurity,
            JwtAuthFilter jwtAuthFilter,
            JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint
    ) throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sessionConfig -> sessionConfig
                        .sessionCreationPolicy(STATELESS)
                )
                .addFilterBefore(jwtAuthFilter, BasicAuthenticationFilter.class)
                .exceptionHandling(ehc -> ehc
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                )
                .authorizeHttpRequests(request -> request
                        .requestMatchers(POST, "/api/v1/users").permitAll() //sign-up endpoint
                        .requestMatchers(GET, "/api/v1/users/email-verification").permitAll()
                        .requestMatchers(POST, "/api/v1/users/resend-email-verification").permitAll()
                        .requestMatchers(POST, "/api/v1/users/reset-password-request").permitAll()
                        .requestMatchers(POST, "/api/v1/users/reset-password").permitAll()
                        .requestMatchers("/api/v1/login").permitAll()
                        .requestMatchers("/api/v1/users/**").authenticated()
                );

        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            TomUserDetailsManager userDetailsManager,
            PasswordEncoder passwordEncoder
    ) {
        var authProvider = new TomDaoAuthenticationProvider(userDetailsManager, passwordEncoder);

        return new ProviderManager(authProvider);
    }
}
