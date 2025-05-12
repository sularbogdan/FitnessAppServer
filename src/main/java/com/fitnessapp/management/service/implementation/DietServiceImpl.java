package com.fitnessapp.management.service.implementation;

import com.fitnessapp.management.config.MapperConfig;
import com.fitnessapp.management.exception.UserNotFoundException;
import com.fitnessapp.management.repository.FavoriteMealRepository;
import com.fitnessapp.management.repository.UserRepository;
import com.fitnessapp.management.repository.dto.DietPlanDTO;
import com.fitnessapp.management.repository.dto.FavoriteMealDTO;
import com.fitnessapp.management.repository.entity.DietPlanEntity;
import com.fitnessapp.management.repository.entity.FavoriteMeal;
import com.fitnessapp.management.repository.entity.User;
import com.fitnessapp.management.service.DietService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class DietServiceImpl implements DietService {


    private final MapperConfig mapper;
    private final UserRepository userRepository;
    private final FavoriteMealRepository favoriteMealRepository;

    public void saveFavoriteMeal(FavoriteMealDTO favoriteMealDTO, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        FavoriteMeal meal = FavoriteMeal.builder()
                .title(favoriteMealDTO.getTitle())
                .imageType(favoriteMealDTO.getImageType())
                .sourceUrl(favoriteMealDTO.getSourceUrl())
                .readyInMinutes(favoriteMealDTO.getReadyInMinutes())
                .user(user)
                .build();

        favoriteMealRepository.save(meal);
    }

    @Override
    public List<FavoriteMealDTO> getFavoriteMealsByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        List<FavoriteMeal> meals = favoriteMealRepository.findAllByUser(user);
        return mapper.mapToList(meals, FavoriteMealDTO.class);
    }

    @Override
    public void deleteFavoriteMeal(Long userId, Long mealId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        FavoriteMeal meal = favoriteMealRepository.findById(mealId)
                .orElseThrow(() -> new RuntimeException("Meal not found!"));

        favoriteMealRepository.delete(meal);
    }


}
