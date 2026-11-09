package org.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Application configuration for password encryption.
 * @Configuration tells Spring to read this class at startup.
 */
@Configuration
public class PasswordConfig {

    /**
     * Creates the password encoder used everywhere in the app.
     * BCrypt encrypts passwords with a random salt, so the same password
     * gives a different hash each time. The passwords are never stored in plain text.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
