package com.fitnessapp.management.service.implementation;

import com.fitnessapp.management.repository.dto.PlanSelectionRequest;
import com.fitnessapp.management.repository.entity.SubscriptionPlan;
import com.fitnessapp.management.service.StripeService;
import com.fitnessapp.management.service.SubscriptionPlanService;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.stereotype.Service;

@Service
public class StripeServiceImpl implements StripeService {

    private final SubscriptionPlanService planService;

    public StripeServiceImpl(SubscriptionPlanService planService) {
        this.planService = planService;
    }

    @Override
    public Session createCheckoutSession(PlanSelectionRequest request) throws Exception {
        SubscriptionPlan plan = planService.getById(request.getPlanId());
        System.out.println("Creating session for userId: " + request.getUserId() + " planId: " + plan.getId());
        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:8080/success")
                .setCancelUrl("http://localhost:8080/cancel")
                .setClientReferenceId(String.valueOf(request.getUserId()))
                .putMetadata("planId", String.valueOf(plan.getId()))
                .addLineItem(
                        SessionCreateParams.LineItem.builder()
                                .setQuantity(1L)
                                .setPriceData(
                                        SessionCreateParams.LineItem.PriceData.builder()
                                                .setCurrency("ron")
                                                .setUnitAmount(plan.getPriceInRon() * 100)
                                                .setProductData(
                                                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                .setName(plan.getName())
                                                                .build()
                                                )
                                                .build()
                                )
                                .build()
                )
                .build();

        return Session.create(params);
    }
}
