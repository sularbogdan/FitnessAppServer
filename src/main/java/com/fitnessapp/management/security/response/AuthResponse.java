package com.fitnessapp.management.security.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AuthResponse {
    private String username;
    private String message;
    private String fullName;

    public AuthResponse(String username, String message, String fullName) {
        this.username = username;
        this.message = message;
        this.fullName = fullName;
    }
}
