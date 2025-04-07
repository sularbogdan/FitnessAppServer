package com.fitnessapp.management.utils;

import com.fitnessapp.management.repository.UserRepository;
import com.fitnessapp.management.repository.entity.User;
import com.fitnessapp.management.repository.entity.enums.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminUserInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AdminUserInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {

        String username = "sularvlad";
        if (userRepository.findByUsername(username).isEmpty()) {
            User admin = new User();
            admin.setUsername("sularvlad");
            admin.setEmail("sularvlad@yahoo.com");
            admin.setPassword(passwordEncoder.encode("vlad123"));
            admin.setRole(Role.ADMIN);
            admin.setFirstName("Sular");
            admin.setLastName("Vlad");
            admin.setActive(true);

            userRepository.save(admin);
            System.out.println("Admin user created.");
        } else {
            System.out.println("Admin user already exists.");
        }
    }
}
