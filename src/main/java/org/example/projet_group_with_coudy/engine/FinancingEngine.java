package org.example.projet_group_with_coudy.engine;

import org.example.projet_group_with_coudy.model.ApprovalStatus;
import org.example.projet_group_with_coudy.model.FinancingPlan;
import org.example.projet_group_with_coudy.model.StudentApplication;
import org.example.projet_group_with_coudy.model.StudyCycle;
import org.example.projet_group_with_coudy.port.HousingAidPort;
import org.example.projet_group_with_coudy.port.TreasuryPort;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.EnumMap;
import java.util.Map;

public class FinancingEngine {

    private static final int SCALE = 2;
    private static final BigDecimal SEUIL_PAUVRETE_REVENU_ANNUEL = new BigDecimal("1200000");
    private static final BigDecimal DISTANCE_MINIMALE_MOBILITE_KM = new BigDecimal("50");
    private static final BigDecimal BOURSE_MOBILITE_BASE = new BigDecimal("20000");

    private static final Map<StudyCycle, BigDecimal> FRAIS_INSCRIPTION_PAR_CYCLE = new EnumMap<>(StudyCycle.class);

    static {
        FRAIS_INSCRIPTION_PAR_CYCLE.put(StudyCycle.LICENCE, new BigDecimal("100000"));
        FRAIS_INSCRIPTION_PAR_CYCLE.put(StudyCycle.MASTER, new BigDecimal("150000"));
        FRAIS_INSCRIPTION_PAR_CYCLE.put(StudyCycle.DOCTORAT, new BigDecimal("200000"));
    }

    private final HousingAidPort housingAidPort;
    private final TreasuryPort treasuryPort;

    public FinancingEngine(HousingAidPort housingAidPort, TreasuryPort treasuryPort) {
        this.housingAidPort = housingAidPort;
        this.treasuryPort = treasuryPort;
    }

    public FinancingPlan calculate(StudentApplication application) {
        BigDecimal tuitionFees = calculateTuitionFees(application);
        BigDecimal grossMonthlyScholarship = calculateMobilityScholarship(application);
        BigDecimal housingAidDeduction = BigDecimal.ZERO.setScale(SCALE);
        BigDecimal monthlyScholarship = grossMonthlyScholarship.subtract(housingAidDeduction);

        return new FinancingPlan(
                application.studentId(),
                tuitionFees,
                grossMonthlyScholarship,
                housingAidDeduction,
                monthlyScholarship,
                ApprovalStatus.APPROUVE);
    }

    private BigDecimal calculateTuitionFees(StudentApplication application) {
        if (application.familyAnnualIncome().compareTo(SEUIL_PAUVRETE_REVENU_ANNUEL) < 0) {
            return BigDecimal.ZERO.setScale(SCALE);
        }
        return FRAIS_INSCRIPTION_PAR_CYCLE.get(application.studyCycle()).setScale(SCALE, RoundingMode.HALF_UP);
    }

    private BigDecimal calculateMobilityScholarship(StudentApplication application) {
        if (application.homeDistanceKm().compareTo(DISTANCE_MINIMALE_MOBILITE_KM) <= 0) {
            return BigDecimal.ZERO.setScale(SCALE);
        }
        BigDecimal coefficientSocial = calculateSocialCoefficient(application.familyAnnualIncome());
        return BOURSE_MOBILITE_BASE.multiply(coefficientSocial).setScale(SCALE, RoundingMode.HALF_UP);
    }

    private BigDecimal calculateSocialCoefficient(BigDecimal familyAnnualIncome) {
        if (familyAnnualIncome.compareTo(SEUIL_PAUVRETE_REVENU_ANNUEL) <= 0) {
            return new BigDecimal("2.0");
        }
        if (familyAnnualIncome.compareTo(SEUIL_PAUVRETE_REVENU_ANNUEL.multiply(BigDecimal.valueOf(2))) <= 0) {
            return new BigDecimal("1.5");
        }
        if (familyAnnualIncome.compareTo(SEUIL_PAUVRETE_REVENU_ANNUEL.multiply(BigDecimal.valueOf(4))) <= 0) {
            return new BigDecimal("1.0");
        }
        return new BigDecimal("0.5");
    }
}
