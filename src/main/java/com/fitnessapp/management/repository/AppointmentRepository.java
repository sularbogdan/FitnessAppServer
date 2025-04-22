package com.fitnessapp.management.repository;


import com.fitnessapp.management.repository.entity.AppointmentRequest;
import com.fitnessapp.management.repository.entity.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<AppointmentRequest, Long> {
    List<AppointmentRequest> findByTrainerIdAndDate(Long trainerId, LocalDate date);
    List<AppointmentRequest> findByStatus(Status status);
}
