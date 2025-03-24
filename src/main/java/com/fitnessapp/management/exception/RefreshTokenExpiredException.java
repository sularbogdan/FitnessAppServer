package com.fitnessapp.management.exception;

import com.fitnessapp.management.repository.entity.RefreshToken;
import lombok.Getter;

@Getter
public class RefreshTokenExpiredException extends RuntimeException {

    private final RefreshToken refreshToken;

    public RefreshTokenExpiredException(RefreshToken refreshToken, String message) {
        super(message);
        this.refreshToken = refreshToken;
    }
}
