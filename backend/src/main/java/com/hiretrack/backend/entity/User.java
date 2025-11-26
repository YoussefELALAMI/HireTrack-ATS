package com.hiretrack.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

import com.hiretrack.backend.enums.Role;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;


    @Getter
    @Column(name = "first_name", length = 100)
    private String firstName;

    @Column(name = "last_name", length = 100)
    private String lastName;

    @Setter
    @Getter
    @Column(name = "email", length = 150, unique = true)
    private String email;

    @Setter
    @Getter
    @Column(name = "password_hash", length = 255)
    private String passwordHash;

    @Setter
    @Getter
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "company_name", length = 150)
    private String companyName;

    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Getter
    @OneToMany(mappedBy = "createdBy")
    private List<Job> jobs;

    public User() {}
}
