CREATE TABLE interviews (
    interview_id SERIAL PRIMARY KEY,
    application_id INT NOT NULL,
    interviewer_id INT NOT NULL,
    interview_date TIMESTAMP NOT NULL,
    interview_type VARCHAR(20),
    feedback TEXT,
    rating INT,
    outcome VARCHAR(20),

    CONSTRAINT fk_interview_application
        FOREIGN KEY (application_id)
        REFERENCES applications(application_id)
        ON DELETE CASCADE,

    CONSTRAINT fk_interview_interviewer
        FOREIGN KEY (interviewer_id)
        REFERENCES users(user_id)
        ON DELETE SET NULL
);