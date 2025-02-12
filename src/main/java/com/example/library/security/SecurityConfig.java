package com.example.library.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Disable all default security behavior
        http
                .authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll()) // Allow all requests
                .csrf().disable() // Disable CSRF protection
                .formLogin().disable() // Disable the default login page
                .httpBasic().disable(); // Disable basic authentication

        return http.build();
    }

}