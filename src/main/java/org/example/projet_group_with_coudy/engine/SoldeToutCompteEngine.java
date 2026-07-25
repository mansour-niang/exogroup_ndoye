package org.example.projet_group_with_coudy.engine;

import org.example.projet_group_with_coudy.model.EmployeeDepartureFile;
import org.example.projet_group_with_coudy.model.SeveranceStatement;
import org.example.projet_group_with_coudy.port.LaborInspectionPort;
import org.example.projet_group_with_coudy.port.TaxAdministrationPort;
import org.example.projet_group_with_coudy.port.TaxableAmounts;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class SoldeToutCompteEngine {

    private static final int JOURS_OUVRES_PAR_MOIS = 21;
    private static final int SCALE = 2;

    private final TaxAdministrationPort taxAdministrationPort;
    private final LaborInspectionPort laborInspectionPort;

    public SoldeToutCompteEngine(TaxAdministrationPort taxAdministrationPort, LaborInspectionPort laborInspectionPort) {
        this.taxAdministrationPort = taxAdministrationPort;
        this.laborInspectionPort = laborInspectionPort;
    }

    public SeveranceStatement calculate(EmployeeDepartureFile file) {
        BigDecimal paidLeaveIndemnity = calculatePaidLeaveIndemnity(file);
        BigDecimal seniorityBonus = BigDecimal.ZERO.setScale(SCALE);
        BigDecimal grossAmount = paidLeaveIndemnity.add(seniorityBonus);

        BigDecimal taxWithholding = taxAdministrationPort
                .calculateWithholding(new TaxableAmounts(grossAmount, seniorityBonus))
                .setScale(SCALE, RoundingMode.HALF_UP);

        BigDecimal noticeViolationPenalty = BigDecimal.ZERO.setScale(SCALE);
        BigDecimal netAmount = grossAmount.subtract(taxWithholding).subtract(noticeViolationPenalty);

        return new SeveranceStatement(
                file.employeeId(),
                paidLeaveIndemnity,
                seniorityBonus,
                noticeViolationPenalty,
                grossAmount,
                taxWithholding,
                netAmount,
                false);
    }

    private BigDecimal calculatePaidLeaveIndemnity(EmployeeDepartureFile file) {
        BigDecimal dailyRate = file.baseMonthlySalary()
                .divide(BigDecimal.valueOf(JOURS_OUVRES_PAR_MOIS), SCALE, RoundingMode.HALF_UP);
        return dailyRate.multiply(BigDecimal.valueOf(file.remainingLeaveDays()));
    }
}
