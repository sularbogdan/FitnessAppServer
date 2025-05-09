package com.fitnessapp.management.repository.dto;

import lombok.Data;

@Data
public class MealDTO {
    private int id;
    private String title;
    private String imageType;
    private String sourceUrl;
    private int readyInMinutes;
}
