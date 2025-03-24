package com.fitnessapp.management.repository.dto;

import com.fitnessapp.management.repository.entity.enums.Role;
import lombok.Data;

@Data
public class UserRequestDTO {
    private Long id;
    private String username;
    private String email;
    private String password;
}
