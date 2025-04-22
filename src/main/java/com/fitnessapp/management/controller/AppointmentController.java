package com.fitnessapp.management.controller;

import com.fitnessapp.management.repository.dto.AppointmentDTO;
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

    private final AppointmentService service;

    @GetMapping("/availability")
    public List<LocalTime> getAvailableHours(@RequestParam Long trainerId, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return service.getAvailableHours(trainerId, date);
    }

    @PostMapping
    public AppointmentRequest requestAppointment(@RequestBody AppointmentDTO dto) {
        return service.requestAppointment(dto.getUserId(), dto.getTrainerId(), dto.getDate(), dto.getTime());
    }

    @GetMapping("/pending")
    public List<AppointmentRequest> getPendingRequests() {
        return service.getPendingRequests();
    }

    @PutMapping("/{id}")
    public AppointmentRequest updateStatus(@PathVariable Long id, @RequestParam Status status) {
        return service.updateStatus(id, status);
    }
}
