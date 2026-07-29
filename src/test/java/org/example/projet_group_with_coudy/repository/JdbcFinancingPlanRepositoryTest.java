package org.example.projet_group_with_coudy.repository;

import org.example.projet_group_with_coudy.model.ApprovalStatus;
import org.example.projet_group_with_coudy.model.FinancingPlan;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class JdbcFinancingPlanRepositoryTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Test
    void insere_le_plan_de_financement_dans_la_table() {
        FinancingPlan plan = new FinancingPlan(
                "etu-1", new BigDecimal("100000.00"), new BigDecimal("120000.00"),
                new BigDecimal("0.00"), new BigDecimal("120000.00"), ApprovalStatus.APPROUVE);

        JdbcFinancingPlanRepository repository = new JdbcFinancingPlanRepository(jdbcTemplate);
        repository.save(plan);

        verify(jdbcTemplate).update(
                "INSERT INTO financing_plan "
                        + "(student_id, tuition_fees, gross_monthly_scholarship, housing_aid_deduction, "
                        + "monthly_scholarship, approval_status) "
                        + "VALUES (?, ?, ?, ?, ?, ?)",
                "etu-1", new BigDecimal("100000.00"), new BigDecimal("120000.00"),
                new BigDecimal("0.00"), new BigDecimal("120000.00"), "APPROUVE");
    }
}
