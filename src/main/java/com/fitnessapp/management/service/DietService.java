package com.fitnessapp.management.service;

import com.fitnessapp.management.repository.dto.DietPlanDTO;
import com.fitnessapp.management.repository.dto.FavoriteMealDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DietService {

    void saveFavoriteMeal(FavoriteMealDTO dto, Long userId);
    List<FavoriteMealDTO> getFavoriteMealsByUserId(Long userId);
    void deleteFavoriteMeal(Long userId, Long mealId);
}
