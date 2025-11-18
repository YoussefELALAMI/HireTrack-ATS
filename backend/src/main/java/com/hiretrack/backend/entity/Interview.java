package com.hiretrack.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "interviews")
public class Interview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "interview_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "application_id", nullable = false)
    private Application application;

    @ManyToOne
    @JoinColumn(name = "interviewer_id", nullable = false)
    private User interviewer;

    @Column(name = "interview_date")
    private LocalDateTime interviewDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "interview_type", length = 20)
    private InterviewType interviewType;

    @Column(columnDefinition = "TEXT")
    private String feedback;

    private Integer rating;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Outcome outcome;

    public enum InterviewType {
        TECHNICAL, HR, FINAL
    }

    public enum Outcome {
        PASSED, FAILED, PENDING
    }
}
