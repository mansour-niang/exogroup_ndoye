package org.example.projet_group_with_coudy.model;

import java.math.BigDecimal;

/**
 * Detail financier final de l'allocation de subvention calculee par le
 * moteur. Record immuable.
 */
public record SubsidyAllocation(
        String farmId,
        BigDecimal baseSubsidy,
        BigDecimal ecologicalBonus,
        BigDecimal underproductionPenalty,
        BigDecimal emergencyFund,
        BigDecimal finalAmount,
        AllocationStatus allocationStatus
) {
}
