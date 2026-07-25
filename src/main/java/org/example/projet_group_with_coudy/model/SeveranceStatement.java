package org.example.projet_group_with_coudy.model;

import java.math.BigDecimal;

/**
 * Detail financier final du solde de tout compte calcule par le moteur.
 * Record immuable.
 */
public record SeveranceStatement(
        String employeeId,
        BigDecimal paidLeaveIndemnity,
        BigDecimal seniorityBonus,
        BigDecimal noticeViolationPenalty,
        BigDecimal grossAmount,
        BigDecimal taxWithholding,
        BigDecimal netAmount,
        boolean auditFlagged
) {
}
