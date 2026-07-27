package org.example.projet_group_with_coudy.repository;

import org.example.projet_group_with_coudy.model.AllocationStatus;
import org.example.projet_group_with_coudy.model.SubsidyAllocation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class JdbcSubsidyAllocationRepositoryTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Test
    void insere_l_allocation_de_subvention_dans_la_table() {
        SubsidyAllocation allocation = new SubsidyAllocation(
                "farm-1", new BigDecimal("1000000.00"), new BigDecimal("150000.00"),
                new BigDecimal("0.00"), new BigDecimal("0.00"),
                new BigDecimal("1150000.00"), AllocationStatus.ALLOUE);

        JdbcSubsidyAllocationRepository repository = new JdbcSubsidyAllocationRepository(jdbcTemplate);
        repository.save(allocation);

        verify(jdbcTemplate).update(
                "INSERT INTO subsidy_allocation "
                        + "(farm_id, base_subsidy, ecological_bonus, underproduction_penalty, "
                        + "emergency_fund, final_amount, allocation_status) "
                        + "VALUES (?, ?, ?, ?, ?, ?, ?)",
                "farm-1", new BigDecimal("1000000.00"), new BigDecimal("150000.00"),
                new BigDecimal("0.00"), new BigDecimal("0.00"),
                new BigDecimal("1150000.00"), "ALLOUE");
    }
}
