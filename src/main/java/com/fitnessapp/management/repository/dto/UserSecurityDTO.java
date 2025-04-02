package com.fitnessapp.management.repository.dto;

import com.fitnessapp.management.repository.entity.enums.Role;
import lombok.Data;

@Data
public class UserSecurityDTO {
    private String username;
    private Long id;
    private String email;
    private String avatarId;
    private String firstName;
    private String lastName;
}
