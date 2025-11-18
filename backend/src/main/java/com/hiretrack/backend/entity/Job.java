package com.hiretrack.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "jobs")
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "job_id")
    private Long id;

    @Column(length = 150)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(length = 100)
    private String department;

    @Enumerated(EnumType.STRING)
    @Column(name = "employment_type", length = 20)
    private EmploymentType employmentType;

    @Column(length = 100)
    private String location;

    @Column(name = "salary_range", length = 50)
    private String salaryRange;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Status status;

    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "job")
    private List<Application> applications;

    public enum EmploymentType {
        FULL_TIME, PART_TIME, INTERNSHIP, CONTRACT
    }

    public enum Status {
        OPEN, CLOSED, DRAFT
    }
}

