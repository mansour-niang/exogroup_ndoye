CREATE TABLE IF NOT EXISTS severance_statement (
    id BIGSERIAL PRIMARY KEY,
    employee_id VARCHAR(64) NOT NULL,
    paid_leave_indemnity NUMERIC(18,2) NOT NULL,
    seniority_bonus NUMERIC(18,2) NOT NULL,
    notice_violation_penalty NUMERIC(18,2) NOT NULL,
    gross_amount NUMERIC(18,2) NOT NULL,
    tax_withholding NUMERIC(18,2) NOT NULL,
    net_amount NUMERIC(18,2) NOT NULL,
    audit_flagged BOOLEAN NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT now()
);
