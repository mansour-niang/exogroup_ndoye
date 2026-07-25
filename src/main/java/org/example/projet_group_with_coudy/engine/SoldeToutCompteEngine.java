package org.example.projet_group_with_coudy.engine;

import org.example.projet_group_with_coudy.model.DepartureReason;
import org.example.projet_group_with_coudy.model.EmployeeDepartureFile;
import org.example.projet_group_with_coudy.model.SeveranceStatement;
import org.example.projet_group_with_coudy.port.LaborInspectionPort;
import org.example.projet_group_with_coudy.port.TaxAdministrationPort;
import org.example.projet_group_with_coudy.port.TaxableAmounts;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Period;
import java.util.Set;

public class SoldeToutCompteEngine {

    private static final int JOURS_OUVRES_PAR_MOIS = 21;
    private static final int SCALE = 2;
    private static final int ANNEES_SEUIL_PRIME = 5;
    private static final BigDecimal TAUX_PRIME_JUSQU_A_5_ANS = new BigDecimal("0.10");
    private static final BigDecimal TAUX_PRIME_AU_DELA_DE_5_ANS = new BigDecimal("0.15");
    private static final Set<DepartureReason> MOTIFS_SANS_PRIME_ANCIENNETE =
            Set.of(DepartureReason.DEMISSION, DepartureReason.LICENCIEMENT_FAUTE_GRAVE);
    private static final BigDecimal SEUIL_AUDIT = new BigDecimal("30000000.00");

    private final TaxAdministrationPort taxAdministrationPort;
    private final LaborInspectionPort laborInspectionPort;

    public SoldeToutCompteEngine(TaxAdministrationPort taxAdministrationPort, LaborInspectionPort laborInspectionPort) {
        this.taxAdministrationPort = taxAdministrationPort;
        this.laborInspectionPort = laborInspectionPort;
    }

    public SeveranceStatement calculate(EmployeeDepartureFile file) {
        BigDecimal paidLeaveIndemnity = calculatePaidLeaveIndemnity(file);
        BigDecimal seniorityBonus = calculateSeniorityBonus(file);
        BigDecimal grossAmount = paidLeaveIndemnity.add(seniorityBonus);

        BigDecimal taxWithholding = taxAdministrationPort
                .calculateWithholding(new TaxableAmounts(grossAmount, seniorityBonus))
                .setScale(SCALE, RoundingMode.HALF_UP);

        BigDecimal noticeViolationPenalty = calculateNoticeViolationPenalty(file);
        BigDecimal netAmount = grossAmount.subtract(taxWithholding).subtract(noticeViolationPenalty);

        boolean auditFlagged = netAmount.compareTo(SEUIL_AUDIT) > 0;
        if (auditFlagged) {
            laborInspectionPort.reportSeveranceForAudit(file.employeeId(), netAmount);
        }

        return new SeveranceStatement(
                file.employeeId(),
                paidLeaveIndemnity,
                seniorityBonus,
                noticeViolationPenalty,
                grossAmount,
                taxWithholding,
                netAmount,
                auditFlagged);
    }

    private BigDecimal calculatePaidLeaveIndemnity(EmployeeDepartureFile file) {
        BigDecimal dailyRate = file.baseMonthlySalary()
                .divide(BigDecimal.valueOf(JOURS_OUVRES_PAR_MOIS), SCALE, RoundingMode.HALF_UP);
        return dailyRate.multiply(BigDecimal.valueOf(file.remainingLeaveDays()));
    }

    private BigDecimal calculateSeniorityBonus(EmployeeDepartureFile file) {
        if (MOTIFS_SANS_PRIME_ANCIENNETE.contains(file.reason())) {
            return BigDecimal.ZERO.setScale(SCALE);
        }

        int completedYears = Period.between(file.hireDate(), file.departureDate()).getYears();
        int yearsAt10Percent = Math.min(completedYears, ANNEES_SEUIL_PRIME);
        int yearsAt15Percent = Math.max(0, completedYears - ANNEES_SEUIL_PRIME);

        BigDecimal bonusRate = BigDecimal.valueOf(yearsAt10Percent).multiply(TAUX_PRIME_JUSQU_A_5_ANS)
                .add(BigDecimal.valueOf(yearsAt15Percent).multiply(TAUX_PRIME_AU_DELA_DE_5_ANS));

        return file.baseMonthlySalary().multiply(bonusRate).setScale(SCALE, RoundingMode.HALF_UP);
    }

    private BigDecimal calculateNoticeViolationPenalty(EmployeeDepartureFile file) {
        boolean penaliteApplicable = file.reason() == DepartureReason.DEMISSION && !file.noticePeriodRespected();
        return penaliteApplicable
                ? file.baseMonthlySalary().setScale(SCALE, RoundingMode.HALF_UP)
                : BigDecimal.ZERO.setScale(SCALE);
    }
}
