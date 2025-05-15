package com.fitnessapp.management.repository;

import com.fitnessapp.management.repository.entity.Memberships;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
    public interface MembershipRepository extends JpaRepository<Memberships, Long> {
        Memberships findByUserId(Long userId);
    }

