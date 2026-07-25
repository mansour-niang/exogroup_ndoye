package org.example.projet_group_with_coudy.repository;

import org.example.projet_group_with_coudy.model.SeveranceStatement;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class JdbcSeveranceStatementRepositoryTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Test
    void insere_le_solde_de_tout_compte_dans_la_table() {
        SeveranceStatement statement = new SeveranceStatement(
                "emp-1", new BigDecimal("100000.00"), new BigDecimal("200000.00"),
                new BigDecimal("0.00"), new BigDecimal("300000.00"),
                new BigDecimal("15000.00"), new BigDecimal("285000.00"), false);

        JdbcSeveranceStatementRepository repository = new JdbcSeveranceStatementRepository(jdbcTemplate);
        repository.save(statement);

        verify(jdbcTemplate).update(
                "INSERT INTO severance_statement "
                        + "(employee_id, paid_leave_indemnity, seniority_bonus, notice_violation_penalty, "
                        + "gross_amount, tax_withholding, net_amount, audit_flagged) "
                        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
                "emp-1", new BigDecimal("100000.00"), new BigDecimal("200000.00"),
                new BigDecimal("0.00"), new BigDecimal("300000.00"),
                new BigDecimal("15000.00"), new BigDecimal("285000.00"), false);
    }
}
