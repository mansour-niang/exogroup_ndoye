CREATE TABLE IF NOT EXISTS financing_plan (
    id BIGSERIAL PRIMARY KEY,
    student_id VARCHAR(64) NOT NULL,
    tuition_fees NUMERIC(18,2) NOT NULL,
    gross_monthly_scholarship NUMERIC(18,2) NOT NULL,
    housing_aid_deduction NUMERIC(18,2) NOT NULL,
    monthly_scholarship NUMERIC(18,2) NOT NULL,
    approval_status VARCHAR(32) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT now()
);
