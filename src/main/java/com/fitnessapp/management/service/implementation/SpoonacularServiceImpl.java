package com.fitnessapp.management.service.implementation;


import com.fitnessapp.management.repository.dto.DietPlanDTO;
import com.fitnessapp.management.repository.dto.DietRequestDTO;
import com.fitnessapp.management.service.SpoonacularService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class SpoonacularServiceImpl implements SpoonacularService {

    private final WebClient.Builder webClientBuilder;


    @Value("${spoonacular.api.key}")
    private String apiKey;

    @Override
    public DietPlanDTO generateDiet(DietRequestDTO request) {
        DietPlanDTO first = callSpoonacularApi(request);
        DietPlanDTO second = callSpoonacularApi(request);

        DietPlanDTO result = new DietPlanDTO();
        result.setMeals(Stream.concat(first.getMeals().stream(), second.getMeals().stream())
                .collect(Collectors.toList()));
        result.setTargetCalories(first.getTargetCalories() + second.getTargetCalories());

        return result;
    }

    private DietPlanDTO callSpoonacularApi(DietRequestDTO request) {
        String allergies = String.join(",", request.getAllergies());

        return webClientBuilder.build()
                .get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .host("api.spoonacular.com")
                        .path("/mealplanner/generate")
                        .queryParam("timeFrame", "day")
                        .queryParam("diet", request.getDietType())
                        .queryParam("exclude", allergies)
                        .queryParam("apiKey", apiKey)
                        .build())
                .retrieve()
                .bodyToMono(DietPlanDTO.class)
                .block();
    }



}
