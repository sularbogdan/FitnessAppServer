package com.fitnessapp.management.controller;

import com.fasterxml.jackson.databind.JsonNode;
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
import com.stripe.model.EventDataObjectDeserializer;
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
            if (!"checkout.session.completed".equals(event.getType())) {
                return "Ignored event";
            }

            EventDataObjectDeserializer deserializer = event.getDataObjectDeserializer();
            if (deserializer.getRawJson().isEmpty()) {
                return "Session deserialization failed";
            }

            JsonNode dataObject = objectMapper.readTree(deserializer.getRawJson());
            String clientReferenceId = dataObject.get("client_reference_id").asText();
            String planId = dataObject.get("metadata").get("planId").asText();

            if (clientReferenceId == null || planId == null) {
                return "Missing metadata";
            }

            Long userId = Long.parseLong(clientReferenceId);
            Long subPlanId = Long.parseLong(planId);

            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new UserNotFoundException("User not found"));

            SubscriptionPlan plan = planRepository.findById(subPlanId)
                    .orElseThrow(() -> new PlanNotFoundException("Plan not found"));

            Memberships existing = membershipRepository.findByUser(user);
            if (existing != null) {
                membershipRepository.delete(existing);
            }

            Memberships membership = new Memberships();
            membership.setUser(user);
            membership.setSubscriptionPlan(plan);
            membership.setStartDate(Date.from(Instant.now()));
            membership.setEndDate(Date.from(Instant.now().plusSeconds(30L * 24 * 60 * 60)));
            membership.setPrice(plan.getPriceInRon() / 100f);

            membershipRepository.save(membership);
            return "Received";

        } catch (SignatureVerificationException e) {
            return "Invalid signature";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}
