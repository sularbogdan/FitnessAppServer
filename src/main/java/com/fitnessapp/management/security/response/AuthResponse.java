package com.fitnessapp.management.security.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AuthResponse {
    private String username;
    private String message;
    private String firstName;
    private String lastName;
    private String email;

    public AuthResponse(String username, String message, String firstName, String lastName, String email) {
        this.username = username;
        this.message = message;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }
}
