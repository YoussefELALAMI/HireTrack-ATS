package com.hiretrack.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "candidates")
public class Candidate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "candidate_id")
    private Long id;

    @Column(name = "first_name", length = 100)
    private String firstName;

    @Column(name = "last_name", length = 100)
    private String lastName;

    @Column(length = 150, unique = true)
    private String email;

    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    @Column(name = "resume_url", length = 255)
    private String resumeUrl;

    @Column(name = "linkedin_url", length = 255)
    private String linkedInUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "experience_level", length = 20)
    private ExperienceLevel experienceLevel;

    @Column(length = 100)
    private String location;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "candidate")
    private List<Application> applications;

    public enum ExperienceLevel {
        JUNIOR, MID, SENIOR // corrected the typo
    }
}
