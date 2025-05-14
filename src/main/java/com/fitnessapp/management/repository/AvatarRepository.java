package com.fitnessapp.management.repository;

import com.fitnessapp.management.repository.entity.Avatar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AvatarRepository extends JpaRepository<Avatar, Long> {
    Optional<Avatar> findFirstByFileName(String fileName);



}
