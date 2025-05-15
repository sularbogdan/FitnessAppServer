package com.fitnessapp.management.repository.dto;

import lombok.Data;

@Data
public class PlanSelectionRequest {
    private Long planId;
    private Long userId;
}
