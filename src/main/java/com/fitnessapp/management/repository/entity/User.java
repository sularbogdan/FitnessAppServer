package com.fitnessapp.management.repository.entity;

import com.fitnessapp.management.repository.entity.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "\"user\"")
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "email")
    private String email;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "password")
    private String password;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "is_active")
    private boolean isActive;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Trainers trainer;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Memberships memberships;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Avatar avatar;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private List<Feedback> feedbacks;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RefreshToken> refreshTokens = new ArrayList<>();

    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }

    @Override
    public String toString() {
        return "User{"
                + "id=" + id
                + ", username='" + username + '\''
                + ", email='" + email + '\''
                + ", first_name='" + firstName + '\''
                + ", last_name='" + lastName + '\''
                + ", password='" + password + '\''
                + '}';
    }
}
