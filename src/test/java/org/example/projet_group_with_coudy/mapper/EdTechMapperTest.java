package org.example.projet_group_with_coudy.mapper;

import org.example.projet_group_with_coudy.dto.CycleEtudes;
import org.example.projet_group_with_coudy.dto.DossierEtudiant;
import org.example.projet_group_with_coudy.dto.MentionBaccalaureat;
import org.example.projet_group_with_coudy.dto.PlanFinancement;
import org.example.projet_group_with_coudy.dto.StatutApprobation;
import org.example.projet_group_with_coudy.model.ApprovalStatus;
import org.example.projet_group_with_coudy.model.BaccalaureateMention;
import org.example.projet_group_with_coudy.model.FinancingPlan;
import org.example.projet_group_with_coudy.model.StudentApplication;
import org.example.projet_group_with_coudy.model.StudyCycle;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class EdTechMapperTest {

    private final EdTechMapper mapper = new EdTechMapper();

    @Test
    void convertit_le_dto_de_requete_en_modele_de_domaine() {
        DossierEtudiant dto = new DossierEtudiant(
                "etu-1", CycleEtudes.LICENCE, new BigDecimal("1000000"), new BigDecimal("75"),
                MentionBaccalaureat.TRES_BIEN, false, false);
        dto.moyenneAnneePrecedente(new BigDecimal("17.2"));

        StudentApplication domain = mapper.toDomain(dto);

        assertEquals(new StudentApplication(
                "etu-1", StudyCycle.LICENCE, new BigDecimal("1000000"), new BigDecimal("75"),
                BaccalaureateMention.TRES_BIEN, new BigDecimal("17.2"), false, false), domain);
    }

    @Test
    void convertit_un_dto_sans_moyenne_precedente_en_moyenne_nulle() {
        DossierEtudiant dto = new DossierEtudiant(
                "etu-2", CycleEtudes.LICENCE, new BigDecimal("1000000"), new BigDecimal("75"),
                MentionBaccalaureat.PASSABLE, false, false);

        StudentApplication domain = mapper.toDomain(dto);

        assertNull(domain.previousYearAverage());
    }

    @Test
    void convertit_le_modele_de_domaine_en_dto_de_reponse() {
        FinancingPlan domain = new FinancingPlan(
                "etu-1", new BigDecimal("100000.00"), new BigDecimal("120000.00"),
                new BigDecimal("0.00"), new BigDecimal("120000.00"), ApprovalStatus.APPROUVE);

        PlanFinancement dto = mapper.toDto(domain);

        assertEquals(new PlanFinancement(
                "etu-1", new BigDecimal("100000.00"), new BigDecimal("120000.00"),
                new BigDecimal("0.00"), new BigDecimal("120000.00"), StatutApprobation.APPROUVE), dto);
    }
}
