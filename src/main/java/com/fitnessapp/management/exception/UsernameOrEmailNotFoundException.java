package com.fitnessapp.management.exception;

public class UsernameOrEmailNotFoundException extends RuntimeException {
    public UsernameOrEmailNotFoundException(String message) {
        super(message);
    }
}
