package com.fitnessapp.management.repository.entity;

import com.fitnessapp.management.repository.entity.enums.MembershipType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Entity
@Table(name = "\"memberships\"")
@AllArgsConstructor
@NoArgsConstructor
public class Memberships {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "membership_id")
    private Long id;

    @Column(name = "type")
    private MembershipType membershipType;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "end_date")
    private Date endDate;

    @Column (name = "price")
    private float price;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;
}
