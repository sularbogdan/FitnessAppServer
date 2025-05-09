package com.fitnessapp.management.repository.dto;

import lombok.Data;

import java.util.List;

@Data
public class DietRequestDTO {
    private int currentWeight;
    private int targetWeight;
    private String dietType;
    private List<String> allergies;
}

