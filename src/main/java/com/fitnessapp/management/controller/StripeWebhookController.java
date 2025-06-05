package com.fitnessapp.management.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fitnessapp.management.exception.PlanNotFoundException;
import com.fitnessapp.management.exception.UserNotFoundException;
import com.fitnessapp.management.repository.MembershipRepository;
import com.fitnessapp.management.repository.SubscriptionPlanRepository;
import com.fitnessapp.management.repository.UserRepository;
import com.fitnessapp.management.repository.dto.MembershipDTO;
import com.fitnessapp.management.repository.entity.Memberships;
import com.fitnessapp.management.repository.entity.SubscriptionPlan;
import com.fitnessapp.management.repository.entity.User;
import com.fitnessapp.management.service.UserService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.net.Webhook;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/stripe")
public class StripeWebhookController {

    private final ObjectMapper objectMapper;
    private final MembershipRepository membershipRepository;
    private final UserRepository userRepository;
    private final SubscriptionPlanRepository planRepository;
    private final UserService userService;

    @Value("${stripe.webhook.secret}")
    private String endpointSecret;

    public StripeWebhookController(ObjectMapper objectMapper,
                                   MembershipRepository membershipRepository,
                                   UserRepository userRepository,
                                   SubscriptionPlanRepository planRepository, UserService userService) {
        this.objectMapper = objectMapper;
        this.membershipRepository = membershipRepository;
        this.userRepository = userRepository;
        this.planRepository = planRepository;
        this.userService = userService;
    }

    @Transactional
    @PostMapping("/webhook")
    public String handleStripeWebhook(@RequestHeader("Stripe-Signature") String sigHeader,
                                      @RequestBody String payload) {
        try {
            Event event = Webhook.constructEvent(payload, sigHeader, endpointSecret);
            if (!"checkout.session.completed".equals(event.getType())) {
                return "Ignored event";
            }

            JsonNode eventNode = objectMapper.readTree(payload);
            JsonNode dataObject = eventNode.get("data").get("object");

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
            LocalDate today = LocalDate.now();
            LocalDate endDate = today.plusDays(30);
            LocalDateTime endDateTime = endDate.atTime(23, 59, 59);
            Date finalEndDate = Date.from(endDateTime.atZone(ZoneId.systemDefault()).toInstant());
            membership.setEndDate(finalEndDate);
            membership.setPrice(plan.getPriceInRon() / 100f);

            membershipRepository.save(membership);
            return "Received";

        } catch (SignatureVerificationException e) {
            return "Invalid signature";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }


    @GetMapping("/membership/active")
    public ResponseEntity<MembershipDTO> getActiveMembership() {
        User user = userService.getCurrentUser();
        Memberships membership = membershipRepository.findByUser(user);

        if (membership == null) {
            return ResponseEntity.ok(new MembershipDTO("No active", null));
        }
        MembershipDTO dto = new MembershipDTO(membership.getSubscriptionPlan().getName(), membership.getEndDate());
        return ResponseEntity.ok(dto);
    }

}

