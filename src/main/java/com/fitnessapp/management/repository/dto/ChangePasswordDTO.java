package com.fitnessapp.management.repository.dto;

import lombok.Data;

@Data
public class ChangePasswordDTO {
    private String username;
    private String email;
    private String oldPassword;
    private String newPassword;
}
