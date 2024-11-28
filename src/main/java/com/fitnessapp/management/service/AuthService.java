package com.fitnessapp.management.service;


import com.fitnessapp.management.security.request.LoginRequest;
import com.fitnessapp.management.security.request.RegisterRequest;
import com.fitnessapp.management.security.response.AuthResponse;

public interface AuthService {
    AuthResponse login(LoginRequest loginRequest);
    AuthResponse register(RegisterRequest registerRequest);
}