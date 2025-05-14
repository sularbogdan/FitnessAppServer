package com.fitnessapp.management.repository;

import com.fitnessapp.management.repository.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {


    Optional<RefreshToken> findByToken(String token);


    Optional<RefreshToken> findByUser_Username(String username);
}