package com.fitnessapp.management.repository;

import com.fitnessapp.management.repository.entity.FavoriteMeal;
import com.fitnessapp.management.repository.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoriteMealRepository extends JpaRepository<FavoriteMeal, Long> {
    List<FavoriteMeal> findAllByUser(User user);

}