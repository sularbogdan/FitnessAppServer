package com.fitnessapp.management.controller;

import com.fitnessapp.management.repository.dto.DietPlanDTO;
import com.fitnessapp.management.repository.dto.DietRequestDTO;
import com.fitnessapp.management.repository.dto.FavoriteMealDTO;
import com.fitnessapp.management.service.DietService;
import com.fitnessapp.management.service.SpoonacularService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/diets")
@RequiredArgsConstructor
public class DietController {

    private final SpoonacularService spoonacularService;
    private final DietService dietService;

    @PostMapping("/generate")
    public ResponseEntity<DietPlanDTO> generate(@RequestBody DietRequestDTO request) {
        return ResponseEntity.ok(spoonacularService.generateDiet(request));
    }

    @PostMapping("/save-favorite-meals/{userId}")
    public ResponseEntity<Void> saveFavoriteMeal(@PathVariable Long userId, @RequestBody FavoriteMealDTO mealDto) {
        dietService.saveFavoriteMeal(mealDto, userId);
        return ResponseEntity.ok().build();
    }


    @GetMapping("/favorites-meals/{userId}")
    public ResponseEntity<List<FavoriteMealDTO>> getFavoriteMeals(@PathVariable Long userId) {
        List<FavoriteMealDTO> favorites = dietService.getFavoriteMealsByUserId(userId);
        return ResponseEntity.ok(favorites);
    }

    @DeleteMapping("/delete-favorite-meal/{userId}/{mealId}")
    public ResponseEntity<Void> deleteFavoriteMeal(@PathVariable Long userId, @PathVariable Long mealId) {
        dietService.deleteFavoriteMeal(userId, mealId);
        return ResponseEntity.ok().build();
    }



}
