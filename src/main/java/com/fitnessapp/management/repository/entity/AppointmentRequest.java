package com.fitnessapp.management.repository.entity;


import com.fitnessapp.management.repository.entity.enums.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "appointment_request")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "trainer_id", nullable = false)
    private User trainer;

    private LocalDate date;
    private LocalTime time;

    @Enumerated(EnumType.STRING)
    private Status status;

    private String comment;
}
