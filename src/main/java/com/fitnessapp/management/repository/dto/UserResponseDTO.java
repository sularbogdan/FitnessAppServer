package com.fitnessapp.management.repository.dto;

import com.fitnessapp.management.repository.entity.enums.Role;
import lombok.Data;

@Data
public class UserResponseDTO {
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String role;
    private byte[] avatarData;
    private String avatarFileName;
    private String avatarFileType;
}

