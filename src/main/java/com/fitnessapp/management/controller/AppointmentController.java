package com.fitnessapp.management.controller;

import com.fitnessapp.management.repository.dto.AppointmentDTO;
import com.fitnessapp.management.repository.dto.AppointmentResponseDTO;
import com.fitnessapp.management.repository.entity.AppointmentRequest;
import com.fitnessapp.management.repository.entity.enums.Status;
import com.fitnessapp.management.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    @GetMapping("/availability")
    public List<LocalTime> getAvailableHours(@RequestParam Long trainerId, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return appointmentService.getAvailableHours(trainerId, date);
    }

    @PostMapping
    public AppointmentRequest requestAppointment(@RequestBody AppointmentDTO dto) {
        return appointmentService.requestAppointment(dto.getUserId(), dto.getTrainerId(), dto.getDate(), dto.getTime());
    }

    @GetMapping("/pending")
    public List<AppointmentResponseDTO> getPendingRequests() {
        return appointmentService.getPendingRequests()
                .stream()
                .map(req -> new AppointmentResponseDTO(
                        req.getId(),
                        req.getUser().getFirstName(),
                        req.getUser().getLastName(),
                        req.getDate(),
                        req.getTime(),
                        req.getStatus()

                ))
                .toList();
    }


    @PutMapping("/{id}/approved")
    public AppointmentResponseDTO approveAppointment(@PathVariable Long id) {
        AppointmentRequest updated = appointmentService.updateStatus(id, Status.APPROVED);
        return new AppointmentResponseDTO(
                updated.getId(),
                updated.getUser().getFirstName(),
                updated.getUser().getLastName(),
                updated.getDate(),
                updated.getTime(),
                updated.getStatus()
        );
    }

    @PutMapping("/{id}/rejected")
    public AppointmentResponseDTO rejectAppointment(@PathVariable Long id) {
        AppointmentRequest updated = appointmentService.updateStatus(id, Status.REJECTED);
        return new AppointmentResponseDTO(
                updated.getId(),
                updated.getUser().getFirstName(),
                updated.getUser().getLastName(),
                updated.getDate(),
                updated.getTime(),
                updated.getStatus()
        );
    }

    @DeleteMapping("/{id}")
    public void deleteAppointment(@PathVariable Long id) {
        appointmentService.deleteAppointment(id);
    }

    @PutMapping("/{id}/cancel")
    public AppointmentResponseDTO cancelAppointment(@PathVariable Long id) {
        AppointmentRequest updated = appointmentService.updateStatus(id, Status.CANCELED);
        return new AppointmentResponseDTO(
                updated.getId(),
                updated.getUser().getFirstName(),
                updated.getUser().getLastName(),
                updated.getDate(),
                updated.getTime(),
                updated.getStatus()
        );
    }


}
