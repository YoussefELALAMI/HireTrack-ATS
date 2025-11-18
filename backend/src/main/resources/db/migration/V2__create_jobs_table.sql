CREATE TABLE jobs (
    job_id SERIAL PRIMARY KEY,
    title VARCHAR(150) NOT NULL,
    description TEXT,
    department VARCHAR(100),
    employment_type VARCHAR(20) NOT NULL,
    location VARCHAR(100),
    salary_range VARCHAR(50),
    status VARCHAR(20) NOT NULL,
    created_by INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_jobs_users
        FOREIGN KEY (created_by)
        REFERENCES users(user_id)
        ON DELETE CASCADE
);