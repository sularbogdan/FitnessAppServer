package com.fitnessapp.management.repository;

import com.fitnessapp.management.repository.entity.Memberships;
import com.fitnessapp.management.repository.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
    public interface MembershipRepository extends JpaRepository<Memberships, Long> {
        Memberships findByUser(User user);
    }

