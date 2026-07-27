CREATE TABLE IF NOT EXISTS subsidy_allocation (
    id BIGSERIAL PRIMARY KEY,
    farm_id VARCHAR(64) NOT NULL,
    base_subsidy NUMERIC(18,2) NOT NULL,
    ecological_bonus NUMERIC(18,2) NOT NULL,
    underproduction_penalty NUMERIC(18,2) NOT NULL,
    emergency_fund NUMERIC(18,2) NOT NULL,
    final_amount NUMERIC(18,2) NOT NULL,
    allocation_status VARCHAR(32) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT now()
);
