package com.fitnessapp.management.repository;


import com.fitnessapp.management.repository.entity.AppointmentRequest;
import com.fitnessapp.management.repository.entity.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<AppointmentRequest, Long> {
    List<AppointmentRequest> findByTrainerIdAndDate(Long trainerId, LocalDate date);
    List<AppointmentRequest> findByStatus(Status status);

    @Query("SELECT a FROM AppointmentRequest a JOIN FETCH a.user WHERE a.status = 'PENDING'")
    List<AppointmentRequest> findAllPendingWithUsers();
    List<AppointmentRequest> findAllByUserId(Long userId);

    List<AppointmentRequest> findAllByUserIdAndStatus(Long userId, Status status);

    @Query("SELECT a FROM AppointmentRequest a WHERE a.user.id = :user_id AND a.status = :status AND a.date BETWEEN :startOfWeek AND :endOfWeek")
    List<AppointmentRequest> findApprovedAppointmentsThisWeek(Long user_id, Status status, LocalDate startOfWeek, LocalDate endOfWeek);

}
