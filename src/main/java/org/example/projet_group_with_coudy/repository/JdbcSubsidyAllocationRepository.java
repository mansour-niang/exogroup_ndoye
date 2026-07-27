package org.example.projet_group_with_coudy.repository;

import org.example.projet_group_with_coudy.model.SubsidyAllocation;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcSubsidyAllocationRepository implements SubsidyAllocationRepository {

    private static final String INSERT_SQL = "INSERT INTO subsidy_allocation "
            + "(farm_id, base_subsidy, ecological_bonus, underproduction_penalty, "
            + "emergency_fund, final_amount, allocation_status) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?)";

    private final JdbcTemplate jdbcTemplate;

    public JdbcSubsidyAllocationRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(SubsidyAllocation allocation) {
        jdbcTemplate.update(
                INSERT_SQL,
                allocation.farmId(),
                allocation.baseSubsidy(),
                allocation.ecologicalBonus(),
                allocation.underproductionPenalty(),
                allocation.emergencyFund(),
                allocation.finalAmount(),
                allocation.allocationStatus().name());
    }
}
