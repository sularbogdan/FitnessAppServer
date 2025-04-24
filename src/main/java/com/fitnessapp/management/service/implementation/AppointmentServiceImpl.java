package com.fitnessapp.management.service.implementation;

import com.fitnessapp.management.repository.AppointmentRepository;
import com.fitnessapp.management.repository.UserRepository;
import com.fitnessapp.management.repository.entity.AppointmentRequest;
import com.fitnessapp.management.repository.entity.User;
import com.fitnessapp.management.repository.entity.enums.Status;
import com.fitnessapp.management.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepo;

    @Autowired
    private UserRepository userRepo;

    public List<LocalTime> getAvailableHours(Long trainerId, LocalDate date) {
        List<AppointmentRequest> existing = appointmentRepo.findByTrainerIdAndDate(trainerId, date);
        List<LocalTime> taken = existing.stream().map(AppointmentRequest::getTime).toList();

        List<LocalTime> allHours = IntStream.rangeClosed(9, 17)
                .mapToObj(i -> LocalTime.of(i, 0))
                .collect(Collectors.toList());

        return allHours.stream().filter(h -> !taken.contains(h)).toList();
    }

    public AppointmentRequest requestAppointment(Long userId, Long trainerId, LocalDate date, LocalTime time) {
        User user = userRepo.findById(userId).orElseThrow();
        User trainer = userRepo.findById(trainerId).orElseThrow();

        AppointmentRequest request = new AppointmentRequest();
        request.setUser(user);
        request.setTrainer(trainer);
        request.setDate(date);
        request.setTime(time);

        request.setStatus(Status.PENDING);
        return appointmentRepo.save(request);
    }

    public List<AppointmentRequest> getPendingRequests() {

        return appointmentRepo.findByStatus(Status.PENDING);
    }

    public AppointmentRequest updateStatus(Long id, Status status) {
        AppointmentRequest req = appointmentRepo.findById(id).orElseThrow();
        req.setStatus(status);
        return appointmentRepo.save(req);
    }

    public void deleteAppointment(Long id){
        appointmentRepo.deleteById(id);
    }

}

