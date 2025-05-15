package com.fitnessapp.management.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fitnessapp.management.exception.PlanNotFoundException;
import com.fitnessapp.management.exception.UserNotFoundException;
import com.fitnessapp.management.repository.MembershipRepository;
import com.fitnessapp.management.repository.SubscriptionPlanRepository;
import com.fitnessapp.management.repository.UserRepository;
import com.fitnessapp.management.repository.entity.Memberships;
import com.fitnessapp.management.repository.entity.SubscriptionPlan;
import com.fitnessapp.management.repository.entity.User;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Date;

@RestController
@RequestMapping("/api/stripe")
public class StripeWebhookController {

    private final ObjectMapper objectMapper;
    private final MembershipRepository membershipRepository;
    private final UserRepository userRepository;
    private final SubscriptionPlanRepository planRepository;

    @Value("${stripe.webhook.secret}")
    private String endpointSecret;

    public StripeWebhookController(ObjectMapper objectMapper,
                                   MembershipRepository membershipRepository,
                                   UserRepository userRepository,
                                   SubscriptionPlanRepository planRepository) {
        this.objectMapper = objectMapper;
        this.membershipRepository = membershipRepository;
        this.userRepository = userRepository;
        this.planRepository = planRepository;
    }

    @PostMapping("/webhook")
    public String handleStripeWebhook(@RequestHeader("Stripe-Signature") String sigHeader,
                                      @RequestBody String payload) {
        try {
            Event event = Webhook.constructEvent(payload, sigHeader, endpointSecret);

            System.out.println("Stripe event type: " + event.getType());

            if ("checkout.session.completed".equals(event.getType())) {
                Session session = objectMapper.readValue(event.getData().getObject().toString(), Session.class);

                String clientReferenceId = session.getClientReferenceId();
                String planId = session.getMetadata().get("planId");

                System.out.println("ClientReferenceId: " + clientReferenceId);
                System.out.println("PlanId from metadata: " + planId);

                if (clientReferenceId == null || planId == null) {
                    System.out.println("One of the required IDs is null!");
                    return "Missing metadata";
                }


                User user = userRepository.findById(Long.parseLong(clientReferenceId))
                        .orElseThrow(() -> new UserNotFoundException("User not found"));

                SubscriptionPlan plan = planRepository.findById(Long.parseLong(planId))
                        .orElseThrow(() -> new PlanNotFoundException("Plan not found"));

                Memberships membership = new Memberships();
                membership.setUser(user);
                membership.setSubscriptionPlan(plan);
                membership.setStartDate(Date.from(Instant.now()));
                membership.setEndDate(Date.from(Instant.now().plusSeconds(30L * 24 * 60 * 60)));
                membership.setPrice(plan.getPriceInRon() / 100f);

                membershipRepository.save(membership);
            }

            return "Received";
        } catch (SignatureVerificationException e) {
            return "Invalid signature";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }
}
