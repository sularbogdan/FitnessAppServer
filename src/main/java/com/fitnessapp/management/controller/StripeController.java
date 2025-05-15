package com.fitnessapp.management.controller;

import com.fitnessapp.management.repository.dto.PlanSelectionRequest;
import com.fitnessapp.management.service.StripeService;
import com.stripe.model.checkout.Session;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/payment")

public class StripeController {

    private final StripeService stripeService;

    public StripeController(StripeService stripeService) {
        this.stripeService = stripeService;
    }

    @PostMapping("/create-checkout-session")
    public Map<String, String> createCheckoutSession(@RequestBody PlanSelectionRequest request) throws Exception {
        Session session = stripeService.createCheckoutSession(request);
        return Map.of("url", session.getUrl());
    }
}
