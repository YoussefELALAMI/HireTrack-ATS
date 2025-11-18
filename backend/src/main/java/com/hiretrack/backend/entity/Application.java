package com.hiretrack.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "applications")
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "application_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;

    @ManyToOne
    @JoinColumn(name = "candidate_id", nullable = false)
    private Candidate candidate;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private ApplicationStatus status;

    @Column(name = "applied_at")
    private LocalDateTime appliedAt;

    @Column(length = 50)
    private String source;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @OneToMany(mappedBy = "application")
    private List<Interview> interviews;

    public enum ApplicationStatus {
        APPLIED, IN_REVIEW, INTERVIEWING, REJECTED, HIRED
    }
}

