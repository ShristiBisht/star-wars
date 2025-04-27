package com.example.starwars.star_wars.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())  // Disable CSRF using Customizer
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/login", "/logout", "/api/**").permitAll()  // Allow login and logout without authentication
                .anyRequest().authenticated()  // All other endpoints require authentication
            )
            .formLogin(formLogin -> formLogin
                .loginPage("/login")
                .permitAll()  // Allow access to login page without authentication
            )
            .logout(logout -> logout
                .permitAll()  // Allow logout without authentication
            );
        return http.build();
    }
}
