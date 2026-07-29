package org.example.projet_group_with_coudy.repository;

import org.example.projet_group_with_coudy.model.FinancingPlan;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcFinancingPlanRepository implements FinancingPlanRepository {

    private static final String INSERT_SQL = "INSERT INTO financing_plan "
            + "(student_id, tuition_fees, gross_monthly_scholarship, housing_aid_deduction, "
            + "monthly_scholarship, approval_status) "
            + "VALUES (?, ?, ?, ?, ?, ?)";

    private final JdbcTemplate jdbcTemplate;

    public JdbcFinancingPlanRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(FinancingPlan plan) {
        jdbcTemplate.update(
                INSERT_SQL,
                plan.studentId(),
                plan.tuitionFees(),
                plan.grossMonthlyScholarship(),
                plan.housingAidDeduction(),
                plan.monthlyScholarship(),
                plan.approvalStatus().name());
    }
}
