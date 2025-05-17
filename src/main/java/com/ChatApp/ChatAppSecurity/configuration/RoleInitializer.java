package com.ChatApp.ChatAppSecurity.configuration;



import com.ChatApp.ChatAppSecurity.Repository.RoleRepository;
import com.ChatApp.ChatAppSecurity.dto.Role;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RoleInitializer {

    @Bean
    public CommandLineRunner setupDefaultRoles(RoleRepository roleRepository) {
        return args -> {
            if (!roleRepository.existsByAuthority("ROLE_USER")) {
                roleRepository.save(new Role("ROLE_USER"));
            }

            if (!roleRepository.existsByAuthority("ROLE_ADMIN")) {
                roleRepository.save(new Role("ROLE_ADMIN"));
            }
        };
    }
}

