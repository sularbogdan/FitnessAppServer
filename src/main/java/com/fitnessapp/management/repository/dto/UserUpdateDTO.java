package com.fitnessapp.management.repository.dto;

import lombok.Data;

@Data
public class UserUpdateDTO {
        private Long Id;
        private String username;
        private String fullName;
        private String email;
        private Long avatarId;
        private ImageDTO image;
        private Boolean updatedImage;
}

