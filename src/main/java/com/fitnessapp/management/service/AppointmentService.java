package com.fitnessapp.management.service;

import com.fitnessapp.management.repository.entity.AppointmentRequest;
import com.fitnessapp.management.repository.entity.enums.Status;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface AppointmentService {

    List<LocalTime> getAvailableHours(Long trainerId, LocalDate date);
    AppointmentRequest requestAppointment(Long userId, Long trainerId, LocalDate date, LocalTime time);
    List<AppointmentRequest> getPendingRequests();
    AppointmentRequest updateStatus(Long id, Status status);
}
