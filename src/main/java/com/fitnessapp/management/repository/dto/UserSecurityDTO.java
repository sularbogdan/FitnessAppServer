package com.fitnessapp.management.repository.dto;

import com.fitnessapp.management.repository.entity.enums.Role;
import lombok.Data;

@Data
public class UserSecurityDTO {
    private String username;
    private Long id;
    private Role role;
    private String email;
    private String firstName;
    private String lastName;
    private String avatarId;
    private Boolean isFirstLogin;
}
