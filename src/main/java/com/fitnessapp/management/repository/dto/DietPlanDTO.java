package com.fitnessapp.management.repository.dto;

import lombok.Data;

import java.util.List;

@Data
public class DietPlanDTO {
    private String title;
    private String description;
    private int targetCalories;
    private List<MealDTO> meals;

}
