package org.example.projet_group_with_coudy.repository;

import org.example.projet_group_with_coudy.model.SeveranceStatement;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcSeveranceStatementRepository implements SeveranceStatementRepository {

    private static final String INSERT_SQL = "INSERT INTO severance_statement "
            + "(employee_id, paid_leave_indemnity, seniority_bonus, notice_violation_penalty, "
            + "gross_amount, tax_withholding, net_amount, audit_flagged) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    private final JdbcTemplate jdbcTemplate;

    public JdbcSeveranceStatementRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(SeveranceStatement statement) {
        jdbcTemplate.update(
                INSERT_SQL,
                statement.employeeId(),
                statement.paidLeaveIndemnity(),
                statement.seniorityBonus(),
                statement.noticeViolationPenalty(),
                statement.grossAmount(),
                statement.taxWithholding(),
                statement.netAmount(),
                statement.auditFlagged());
    }
}
