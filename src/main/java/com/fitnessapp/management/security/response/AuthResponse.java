package com.fitnessapp.management.security.response;

import com.fitnessapp.management.repository.dto.UserSecurityDTO;
import lombok.Getter;
import lombok.Setter;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class AuthResponse {
    private Date accessTokenExpiryDate;
    private Date refreshTokenExpiryDate;
    private UserSecurityDTO userData;
}
