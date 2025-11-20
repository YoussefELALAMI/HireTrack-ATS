-- 1️⃣ Create enum types matching Java enums

CREATE TYPE user_role AS ENUM ('ADMIN', 'RECRUITER', 'HIRING_MANAGER', 'INTERVIEWER');

CREATE TYPE application_status AS ENUM ('APPLIED', 'IN_REVIEW', 'INTERVIEWING', 'REJECTED', 'HIRED');

CREATE TYPE experience_level AS ENUM ('JUNIOR', 'MID', 'SENIOR');

CREATE TYPE interview_type AS ENUM ('TECHNICAL', 'HR', 'FINAL');

CREATE TYPE outcome AS ENUM ('PASSED', 'FAILED', 'PENDING');

CREATE TYPE employment_type AS ENUM ('FULL_TIME', 'PART_TIME', 'INTERNSHIP', 'CONTRACT');

CREATE TYPE job_status AS ENUM ('OPEN', 'CLOSED', 'DRAFT');

-- 2️⃣ Alter existing tables to use enums

ALTER TABLE users
  ALTER COLUMN role TYPE user_role
  USING role::user_role;

ALTER TABLE applications
  ALTER COLUMN status TYPE application_status
  USING status::application_status;

ALTER TABLE candidates
  ALTER COLUMN experience_level TYPE experience_level
  USING experience_level::experience_level;

ALTER TABLE interviews
  ALTER COLUMN interview_type TYPE interview_type
  USING interview_type::interview_type;

ALTER TABLE interviews
  ALTER COLUMN outcome TYPE outcome
  USING outcome::outcome;

ALTER TABLE jobs
  ALTER COLUMN employment_type TYPE employment_type
  USING employment_type::employment_type;

ALTER TABLE jobs
  ALTER COLUMN status TYPE job_status
  USING status::job_status;
