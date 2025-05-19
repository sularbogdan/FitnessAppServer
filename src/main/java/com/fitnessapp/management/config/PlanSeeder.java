package com.fitnessapp.management.config;

import com.fitnessapp.management.repository.SubscriptionPlanRepository;
import com.fitnessapp.management.repository.entity.SubscriptionPlan;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class PlanSeeder implements CommandLineRunner {

    private final SubscriptionPlanRepository repository;

    public PlanSeeder(SubscriptionPlanRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) {
        if (repository.count() == 0) {
            repository.save(new SubscriptionPlan(null, "Basic", 3, 20000));
            repository.save(new SubscriptionPlan(null, "Standard", 4, 25000));
            repository.save(new SubscriptionPlan(null, "Premium", 5, 30000));
        }
    }
}
