package com.fitnessapp.management.service.implementation;

import com.fitnessapp.management.exception.UserAlreadyExistsException;
import com.fitnessapp.management.repository.UserRepository;
import com.fitnessapp.management.repository.entity.enums.Role;
import com.fitnessapp.management.repository.entity.User;
import com.fitnessapp.management.security.request.LoginRequest;
import com.fitnessapp.management.security.request.RegisterRequest;
import com.fitnessapp.management.security.response.AuthResponse;
import com.fitnessapp.management.service.AuthService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Override
    public AuthResponse login(LoginRequest loginRequest) {
        Optional<User> userOptional = userRepository.findByUsernameOrEmail(loginRequest.getUsernameOrEmail(), loginRequest.getUsernameOrEmail());
        if (userOptional.isEmpty() || !passwordEncoder.matches(loginRequest.getPassword(), userOptional.get().getPassword())) {
            throw new RuntimeException("Invalid username/email or password!");
        }

        User user = userOptional.get();
        return new AuthResponse(user.getUsername(), "Login successful!", user.getFullName());
    }

    @Override
    public AuthResponse register(RegisterRequest registerRequest) {
        if (userRepository.findByUsernameOrEmail(registerRequest.getUsername(), registerRequest.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("User with the given username or email already exists!");
        }
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setRole(Role.CLIENT);
        user.setFullName(registerRequest.getFullName());
        userRepository.save(user);
        return new AuthResponse(user.getUsername(), "Registration successful!", user.getFullName());
    }
}
