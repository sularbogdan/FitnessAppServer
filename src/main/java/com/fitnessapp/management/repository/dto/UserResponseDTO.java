package com.fitnessapp.management.repository.dto;

import com.fitnessapp.management.repository.entity.enums.Role;
import lombok.Data;

@Data
public class UserResponseDTO {
    private Long id;
    private String username;
    private String email;
    private Role role;
    private AvatarDTO avatar;
    private String firstName;
    private String lastName;
}
