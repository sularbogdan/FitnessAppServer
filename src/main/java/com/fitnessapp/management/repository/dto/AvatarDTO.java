package com.fitnessapp.management.repository.dto;

import lombok.Data;

@Data
public class AvatarDTO {
    private Long id;
    private String fileName;
    private String fileType;
    private String base64Image;
}
