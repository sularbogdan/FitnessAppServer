package com.fitnessapp.management.service.implementation;

import com.fitnessapp.management.exception.PlanNotFoundException;
import com.fitnessapp.management.repository.SubscriptionPlanRepository;
import com.fitnessapp.management.repository.entity.SubscriptionPlan;
import com.fitnessapp.management.service.SubscriptionPlanService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubscriptionPlanServiceImpl implements SubscriptionPlanService {
    private final SubscriptionPlanRepository repository;

    public SubscriptionPlanServiceImpl(SubscriptionPlanRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<SubscriptionPlan> getAllPlans() {
        return repository.findAll();
    }

    @Override
    public SubscriptionPlan getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new PlanNotFoundException("Plan not found"));
    }
}
