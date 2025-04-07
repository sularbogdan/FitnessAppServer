package com.fitnessapp.management.repository;

import com.fitnessapp.management.repository.entity.Avatar;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AvatarRepository extends JpaRepository<Avatar, Long> {
    Avatar findByUser_Username(String username);
}
