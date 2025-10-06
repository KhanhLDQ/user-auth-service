package org.tommap.tomuserloginrestapis.config;

import org.springframework.beans.factory.annotation.Value;
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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.tommap.tomuserloginrestapis.exception.JwtAuthenticationEntryPoint;
import org.tommap.tomuserloginrestapis.filter.JwtAuthFilter;

import java.util.List;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
public class SecurityConfig {
    @Value("${cors.allowed-origins}")
    private List<String> allowedOrigins;

    @Bean
    public SecurityFilterChain filterChain(
            HttpSecurity httpSecurity,
            JwtAuthFilter jwtAuthFilter,
            JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint
    ) throws Exception {
        httpSecurity
                .cors(corsCustomizer ->
                        corsCustomizer.configurationSource(corsConfigurationSource())
                )
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

    /*
        - when allowCredentials is true -> allowedOrigins cannot contain special value '*'
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(allowedOrigins); //should configure specific origins
        config.setAllowedMethods(List.of("*")); //allow all HTTP methods
        config.setAllowedHeaders(List.of("*")); //allow all HTTP headers
        config.setAllowCredentials(true); //allow to include credentials (JWT, cookie, ...)
        config.setMaxAge(3600L); //cache preflight response for an hour per browser

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config); //apply CORS config to all endpoints

        return source;
    }
}
