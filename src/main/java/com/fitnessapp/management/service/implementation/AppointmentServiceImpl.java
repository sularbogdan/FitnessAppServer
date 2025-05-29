package com.fitnessapp.management.service.implementation;

import com.fitnessapp.management.repository.AppointmentRepository;
import com.fitnessapp.management.repository.UserRepository;
import com.fitnessapp.management.repository.entity.AppointmentRequest;
import com.fitnessapp.management.repository.entity.Memberships;
import com.fitnessapp.management.repository.entity.SubscriptionPlan;
import com.fitnessapp.management.repository.entity.User;
import com.fitnessapp.management.repository.entity.enums.Status;
import com.fitnessapp.management.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.WeekFields;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepo;

    @Autowired
    private UserRepository userRepo;

    @Override
    public List<LocalTime> getAvailableHours(Long trainerId, LocalDate date) {
        List<AppointmentRequest> existing = appointmentRepo.findByTrainerIdAndDate(trainerId, date);
        List<LocalTime> taken = existing.stream().map(AppointmentRequest::getTime).toList();

        List<LocalTime> allHours = IntStream.rangeClosed(9, 17)
                .mapToObj(i -> LocalTime.of(i, 0))
                .collect(Collectors.toList());

        return allHours.stream().filter(h -> !taken.contains(h)).toList();
    }
    @Override
    public AppointmentRequest requestAppointment(Long userId, Long trainerId, LocalDate date, LocalTime time) {
        User user = userRepo.findById(userId).orElseThrow();
        User trainer = userRepo.findById(trainerId).orElseThrow();

        Memberships membership = user.getMemberships();
        if (membership == null || membership.getEndDate().before(new Date())) {
            throw new IllegalStateException("No active subscription found.");
        }

        int allowedSessions = membership.getSubscriptionPlan().getWeeklySessions();

        List<AppointmentRequest> userAppointments = appointmentRepo.findAllByUserId(userId);
        WeekFields weekFields = WeekFields.of(Locale.getDefault());

        long currentWeekCount = userAppointments.stream()
                .filter(a -> a.getStatus() == Status.APPROVED)
                .filter(a -> {
                    LocalDate d = a.getDate();
                    return d.get(weekFields.weekOfWeekBasedYear()) == date.get(weekFields.weekOfWeekBasedYear())
                            && d.getYear() == date.getYear();
                })
                .count();

        if (currentWeekCount >= allowedSessions) {
            throw new IllegalStateException("You have reached your weekly session limit.");
        }

        AppointmentRequest request = new AppointmentRequest();
        request.setUser(user);
        request.setTrainer(trainer);
        request.setDate(date);
        request.setTime(time);
        request.setStatus(Status.PENDING);

        return appointmentRepo.save(request);
    }
    @Override
    public List<AppointmentRequest> getPendingRequests() {

        return appointmentRepo.findByStatus(Status.PENDING);
    }
    @Override
    public AppointmentRequest updateStatus(Long id, Status status) {
        AppointmentRequest req = appointmentRepo.findById(id).orElseThrow();
        req.setStatus(status);
        return appointmentRepo.save(req);
    }
    @Override
    public void deleteAppointment(Long id){
        appointmentRepo.deleteById(id);
    }
    @Override
    public List<AppointmentRequest> getAllAppointments(){
         return  appointmentRepo.findAll();
    }
    @Override
    public List<AppointmentRequest> getAppointmentsByUser(Long userId) {
        return appointmentRepo.findAllByUserId(userId);
    }
    @Override
    public boolean hasActiveMembershipWithAvailableSession(User user) {
        Memberships membership = user.getMemberships();
        System.out.println("Membership: " + membership);
        System.out.println("End date: " + (membership != null ? membership.getEndDate() : "null"));

        if (membership == null || membership.getEndDate().before(new Date())) {
            return false;
        }

        SubscriptionPlan plan = membership.getSubscriptionPlan();
        int allowedSessions = plan.getWeeklySessions();


        WeekFields weekFields = WeekFields.of(Locale.getDefault());
        int currentWeek = LocalDate.now().get(weekFields.weekOfWeekBasedYear());
        int currentYear = LocalDate.now().get(weekFields.weekBasedYear());

        List<AppointmentRequest> thisWeekAppointments = appointmentRepo
                .findAllByUserIdAndStatus(user.getId(), Status.APPROVED).stream()
                .filter(a -> {
                    LocalDate date = a.getDate();
                    int appointmentWeek = date.get(weekFields.weekOfWeekBasedYear());
                    int appointmentYear = date.get(weekFields.weekBasedYear());
                    return appointmentWeek == currentWeek && appointmentYear == currentYear;
                })
                .toList();

        return thisWeekAppointments.size() < allowedSessions;
    }
}

