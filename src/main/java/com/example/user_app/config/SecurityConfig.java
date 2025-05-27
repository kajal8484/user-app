package com.example.user_app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

	@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf
                .ignoringRequestMatchers("/h2-console/**", "/api/register")  // Disable CSRF for H2 Console and Register
            )
            .headers(headers -> headers
                .frameOptions(frameOptions -> frameOptions.disable())  // Allow frames for H2 Console
            )
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/register", "/api/register", "/h2-console/**").permitAll()  // Allow public access to register and H2 console
                .anyRequest().authenticated()  // Auth required for everything else
            )
            .httpBasic(Customizer.withDefaults());  // Basic auth for other endpoints

        return http.build();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}