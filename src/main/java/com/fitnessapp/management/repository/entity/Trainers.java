package com.fitnessapp.management.repository.entity;

import com.fitnessapp.management.repository.entity.enums.Specialisation;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@Table(name = "\"trainers\"")
@AllArgsConstructor
@NoArgsConstructor
public class Trainers {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "trainer_id")
    private Long id;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "specialisation")
    @Enumerated(EnumType.STRING)
    private Specialisation specialisation;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

    @OneToMany(mappedBy = "trainer", cascade = CascadeType.ALL)
    private List<Feedback> feedbacks;
}
