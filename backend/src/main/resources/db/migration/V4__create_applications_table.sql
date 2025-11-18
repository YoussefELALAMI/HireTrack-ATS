CREATE TABLE applications (
    application_id SERIAL PRIMARY KEY,
    job_id INT NOT NULL,
    candidate_id INT NOT NULL,
    status VARCHAR(20) NOT NULL,
    applied_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    source VARCHAR(50),
    notes TEXT,

    CONSTRAINT fk_app_job
        FOREIGN KEY (job_id)
        REFERENCES jobs(job_id)
        ON DELETE CASCADE,

    CONSTRAINT fk_app_candidate
        FOREIGN KEY (candidate_id)
        REFERENCES candidates(candidate_id)
        ON DELETE CASCADE
);