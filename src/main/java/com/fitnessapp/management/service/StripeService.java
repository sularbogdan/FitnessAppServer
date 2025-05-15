package com.fitnessapp.management.service;

import com.fitnessapp.management.repository.dto.PlanSelectionRequest;
import com.stripe.model.checkout.Session;
import org.springframework.stereotype.Service;

@Service
public interface StripeService {
    Session createCheckoutSession(PlanSelectionRequest request) throws Exception;
}