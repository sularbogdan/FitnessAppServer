package com.fitnessapp.management.repository.dto;

import com.fitnessapp.management.repository.entity.enums.Status;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;


public record AppointmentResponseDTO(
        Long id,
        String userFirstName,
        String userLastName,
        LocalDate date,
        LocalTime time,
        Status status
) {}
