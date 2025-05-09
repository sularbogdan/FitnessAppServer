package com.fitnessapp.management.service;

import com.fitnessapp.management.repository.dto.DietPlanDTO;
import com.fitnessapp.management.repository.dto.DietRequestDTO;
import org.springframework.stereotype.Service;

@Service
public interface SpoonacularService {
    DietPlanDTO generateDiet(DietRequestDTO request);
}