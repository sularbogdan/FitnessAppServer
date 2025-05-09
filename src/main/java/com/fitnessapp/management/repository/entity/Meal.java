package com.fitnessapp.management.repository.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "meal")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Meal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String imageType;
    private String sourceUrl;
    private int readyInMinutes;

}
