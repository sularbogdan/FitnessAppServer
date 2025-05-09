package com.fitnessapp.management.service;

import com.fitnessapp.management.repository.entity.AppointmentRequest;
import com.fitnessapp.management.repository.entity.enums.Status;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public interface AppointmentService {

    List<LocalTime> getAvailableHours(Long trainerId, LocalDate date);
    AppointmentRequest requestAppointment(Long userId, Long trainerId, LocalDate date, LocalTime time);
    List<AppointmentRequest> getPendingRequests();
    AppointmentRequest updateStatus(Long id, Status status);
    void deleteAppointment(Long id);
    List<AppointmentRequest> getAllAppointments();
    List<AppointmentRequest> getAppointmentsByUser(Long id);
}
