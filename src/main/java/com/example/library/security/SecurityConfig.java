package com.example.library.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/books").permitAll() // Allow public access to get all books.
                        .requestMatchers("/api/books/{id}").permitAll() // Allow public access to get a specific book.
                        .requestMatchers("/api/users/register", "/api/users/login").permitAll() // Public endpoints
                        .requestMatchers("/api/admin/**").hasRole("ADMIN") // Restrict admin endpoints.
                        .anyRequest().permitAll() // All other endpoints need authentication.
                )
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()));

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowCredentials(false);
        configuration.addAllowedOrigin("http://localhost:8080");

        configuration.addAllowedOriginPattern("*"); // Allow all origins
        configuration.addAllowedMethod("*");        // Allow all HTTP methods
        configuration.addAllowedHeader("*");        // Allow all headers
        configuration.setAllowCredentials(true);    // Allow credentials if needed

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


}