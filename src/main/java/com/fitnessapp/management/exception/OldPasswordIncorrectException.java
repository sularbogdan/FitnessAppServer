package com.fitnessapp.management.exception;

public class OldPasswordIncorrectException extends RuntimeException {
    public OldPasswordIncorrectException(String message) {
        super(message);
    }
}
