package com.fitnessapp.management.repository.dto;


import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class AppointmentDTO {
    private Long userId;
    private String userFirstName;
    private String userLastName;
    private Long trainerId;
    private LocalDate date;
    private LocalTime time;

}