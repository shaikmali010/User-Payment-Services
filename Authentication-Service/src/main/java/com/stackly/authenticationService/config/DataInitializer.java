package com.stackly.authenticationService.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.stackly.authenticationService.model.AuthUser;
import com.stackly.authenticationService.repository.AuthUserRepository;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initUsers(AuthUserRepository authUserRepository) {

        return args -> {

            if (authUserRepository.findByUsername("ali").isEmpty()) {

                authUserRepository.save(
                        new AuthUser(
                                null,
                                "ali",
                                "password123",
                                "USER"
                        )
                );
            }

            if (authUserRepository.findByUsername("admin").isEmpty()) {

                authUserRepository.save(
                        new AuthUser(
                                null,
                                "admin",
                                "admin123",
                                "ADMIN"
                        )
                );
            }
        };
    }
}