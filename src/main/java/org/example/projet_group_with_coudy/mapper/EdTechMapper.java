package org.example.projet_group_with_coudy.mapper;

import org.example.projet_group_with_coudy.dto.DossierEtudiant;
import org.example.projet_group_with_coudy.dto.PlanFinancement;
import org.example.projet_group_with_coudy.dto.StatutApprobation;
import org.example.projet_group_with_coudy.model.ApprovalStatus;
import org.example.projet_group_with_coudy.model.BaccalaureateMention;
import org.example.projet_group_with_coudy.model.FinancingPlan;
import org.example.projet_group_with_coudy.model.StudentApplication;
import org.example.projet_group_with_coudy.model.StudyCycle;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class EdTechMapper {

    public StudentApplication toDomain(DossierEtudiant dto) {
        BigDecimal previousYearAverage = dto.getMoyenneAnneePrecedente() != null
                && dto.getMoyenneAnneePrecedente().isPresent()
                ? dto.getMoyenneAnneePrecedente().get()
                : null;

        return new StudentApplication(
                dto.getStudentId(),
                StudyCycle.valueOf(dto.getCycleEtudes().name()),
                dto.getRevenuAnnuelFamilial(),
                dto.getDistanceDomicileKm(),
                BaccalaureateMention.valueOf(dto.getMentionBaccalaureat().name()),
                previousYearAverage,
                dto.getRedoublement(),
                dto.getRedoublementJustifieMedical());
    }

    public PlanFinancement toDto(FinancingPlan plan) {
        return new PlanFinancement(
                plan.studentId(),
                plan.tuitionFees(),
                plan.grossMonthlyScholarship(),
                plan.housingAidDeduction(),
                plan.monthlyScholarship(),
                StatutApprobation.valueOf(plan.approvalStatus().name()));
    }
}
