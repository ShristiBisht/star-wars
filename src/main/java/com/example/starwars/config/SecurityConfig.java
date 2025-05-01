package com.example.starwars.star_wars.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity

/***formLogin(Customizer.withDefaults()): This method now enables form-based login using default settings.
 *  It replaces the old .formLogin() method that was deprecated.
logout(logout -> logout.permitAll()): Configures logout to be allowed for all users, or you can customize it as needed.
csrf(csrf -> csrf.disable()): CSRF protection is disabled in this example.
 If you need CSRF protection for form submissions, you can enable it by using csrf -> csrf.enable().***/
 
public class SecurityConfig {

    // TODO: security
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // http
        //     .csrf(csrf -> csrf.disable())  // Disable CSRF using Customizer
        //     .authorizeHttpRequests(auth -> auth
        //         .requestMatchers("/login", "/logout", "/api/**").permitAll()  // Allow login and logout without authentication
        //         .anyRequest().authenticated()  // All other endpoints require authentication
        //     )
        //     .formLogin(formLogin -> formLogin
        //         .loginPage("/login")
        //         .permitAll()  // Allow access to login page without authentication
        //     )
        //     .logout(logout -> logout
        //         .permitAll()  // Allow logout without authentication
        //     );
        http
        // Enable CORS and disable CSRF (assuming you want a stateless, cross-origin frontend/backend setup)
        .cors(Customizer.withDefaults())
        .csrf(csrf -> csrf.disable())
        // Allow all requests (no login or authentication required)
        .authorizeHttpRequests(auth -> auth
                .requestMatchers("/**").permitAll()  // Allow login and logout without authentication
                .anyRequest().authenticated()  // All other endpoints require authentication
            )
        .formLogin(Customizer.withDefaults()) 
        .logout(logout -> logout.permitAll());
        return http.build();
        
    }
}
