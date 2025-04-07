package com.fitnessapp.management.repository.dto;

import lombok.Data;

import java.util.Base64;

@Data
public class ImageDTO {

    private Long id;
    private byte[] image;
    private String fileName;
    private String fileType;

    public String getBase64Image() {
        return Base64.getEncoder().encodeToString(image);
    }

}