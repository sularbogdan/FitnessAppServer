package com.fitnessapp.management.service;

import com.fitnessapp.management.repository.entity.SubscriptionPlan;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SubscriptionPlanService {
    List<SubscriptionPlan> getAllPlans();
    SubscriptionPlan getById(Long id);
}
