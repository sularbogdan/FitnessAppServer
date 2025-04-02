package com.fitnessapp.management.repository.dto;

import lombok.Data;

@Data
public class UserUpdateDTO {
        private Long Id;
        private String firstName;
        private String lastName;
        private String email;
        private Long avatarId;
        private ImageDTO image;
        private Boolean updatedImage;
}

