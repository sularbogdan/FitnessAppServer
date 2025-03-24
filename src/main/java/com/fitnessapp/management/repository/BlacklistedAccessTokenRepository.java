package com.fitnessapp.management.repository;

import com.fitnessapp.management.repository.entity.BlacklistedAccessToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.Optional;

public interface BlacklistedAccessTokenRepository extends JpaRepository<BlacklistedAccessToken, Long> {

    void deleteAllByExpiryLessThan(Date date);

    Optional<BlacklistedAccessToken> findByToken(String token);


}
