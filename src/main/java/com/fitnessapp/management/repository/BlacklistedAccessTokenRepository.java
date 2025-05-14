package com.fitnessapp.management.repository;

import com.fitnessapp.management.repository.entity.BlacklistedAccessToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
public interface BlacklistedAccessTokenRepository extends JpaRepository<BlacklistedAccessToken, Long> {

    void deleteAllByExpiryLessThan(Date date);

    Optional<BlacklistedAccessToken> findByToken(String token);


}
