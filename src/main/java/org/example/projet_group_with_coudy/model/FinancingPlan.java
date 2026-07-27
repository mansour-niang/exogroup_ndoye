package org.example.projet_group_with_coudy.model;

import java.math.BigDecimal;

/**
 * Plan de financement final calcule pour l'etudiant. Record immuable.
 */
public record FinancingPlan(
        String studentId,
        BigDecimal tuitionFees,
        BigDecimal grossMonthlyScholarship,
        BigDecimal housingAidDeduction,
        BigDecimal monthlyScholarship,
        ApprovalStatus approvalStatus
) {
}
