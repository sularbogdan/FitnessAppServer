package com.fitnessapp.management.repository.dto;

import lombok.Data;

import java.util.List;

@Data
public class FavoriteMealDTO {
    private String title;
    private String imageType;
    private String sourceUrl;
    private int readyInMinutes;
}
