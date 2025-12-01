-- Convert PostgreSQL ENUM columns back to VARCHAR
-- Solution 1: Use JPA EnumType.STRING without DB enums

-- 1️⃣ ALTER COLUMNS BACK TO VARCHAR

ALTER TABLE users
    ALTER COLUMN role TYPE VARCHAR(50)
    USING role::text;

ALTER TABLE applications
    ALTER COLUMN status TYPE VARCHAR(50)
    USING status::text;

ALTER TABLE candidates
    ALTER COLUMN experience_level TYPE VARCHAR(50)
    USING experience_level::text;

ALTER TABLE interviews
    ALTER COLUMN interview_type TYPE VARCHAR(50)
    USING interview_type::text;

ALTER TABLE interviews
    ALTER COLUMN outcome TYPE VARCHAR(50)
    USING outcome::text;

ALTER TABLE jobs
    ALTER COLUMN employment_type TYPE VARCHAR(50)
    USING employment_type::text;

ALTER TABLE jobs
    ALTER COLUMN status TYPE VARCHAR(50)
    USING status::text;

-- 2️⃣ DROP ENUM TYPES (optional but recommended)

DROP TYPE IF EXISTS user_role;
DROP TYPE IF EXISTS application_status;
DROP TYPE IF EXISTS experience_level;
DROP TYPE IF EXISTS interview_type;
DROP TYPE IF EXISTS outcome;
DROP TYPE IF EXISTS employment_type;
DROP TYPE IF EXISTS job_status;
