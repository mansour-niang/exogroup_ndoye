package org.example.projet_group_with_coudy.controller;

import org.example.projet_group_with_coudy.dto.CycleEtudes;
import org.example.projet_group_with_coudy.dto.DossierEtudiant;
import org.example.projet_group_with_coudy.dto.MentionBaccalaureat;
import org.example.projet_group_with_coudy.dto.PlanFinancement;
import org.example.projet_group_with_coudy.dto.StatutApprobation;
import org.example.projet_group_with_coudy.engine.FinancingEngine;
import org.example.projet_group_with_coudy.mapper.EdTechMapper;
import org.example.projet_group_with_coudy.model.ApprovalStatus;
import org.example.projet_group_with_coudy.model.BaccalaureateMention;
import org.example.projet_group_with_coudy.model.FinancingPlan;
import org.example.projet_group_with_coudy.model.StudentApplication;
import org.example.projet_group_with_coudy.model.StudyCycle;
import org.example.projet_group_with_coudy.repository.FinancingPlanRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EdTechControllerTest {

    @Mock
    private FinancingEngine engine;

    @Mock
    private EdTechMapper mapper;

    @Mock
    private FinancingPlanRepository repository;

    @Test
    void delegue_la_conversion_au_mapper_et_le_calcul_au_moteur() {
        DossierEtudiant requestDto = new DossierEtudiant(
                "etu-1", CycleEtudes.LICENCE, new BigDecimal("1000000"), new BigDecimal("75"),
                MentionBaccalaureat.TRES_BIEN, false, false);
        StudentApplication domainApplication = new StudentApplication(
                "etu-1", StudyCycle.LICENCE, new BigDecimal("1000000"), new BigDecimal("75"),
                BaccalaureateMention.TRES_BIEN, null, false, false);
        FinancingPlan plan = new FinancingPlan(
                "etu-1", new BigDecimal("100000.00"), new BigDecimal("168000.00"),
                new BigDecimal("0.00"), new BigDecimal("168000.00"), ApprovalStatus.VALIDATION_MANUELLE_REQUISE);
        PlanFinancement responseDto = new PlanFinancement(
                "etu-1", new BigDecimal("100000.00"), new BigDecimal("168000.00"),
                new BigDecimal("0.00"), new BigDecimal("168000.00"), StatutApprobation.VALIDATION_MANUELLE_REQUISE);

        when(mapper.toDomain(requestDto)).thenReturn(domainApplication);
        when(engine.calculate(domainApplication)).thenReturn(plan);
        when(mapper.toDto(plan)).thenReturn(responseDto);

        EdTechController controller = new EdTechController(engine, mapper, repository);
        ResponseEntity<PlanFinancement> response = controller.calculerPlanFinancement(requestDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(responseDto, response.getBody());
    }

    @Test
    void persiste_le_plan_de_financement_calcule() {
        DossierEtudiant requestDto = new DossierEtudiant(
                "etu-1", CycleEtudes.LICENCE, new BigDecimal("1000000"), new BigDecimal("75"),
                MentionBaccalaureat.TRES_BIEN, false, false);
        StudentApplication domainApplication = new StudentApplication(
                "etu-1", StudyCycle.LICENCE, new BigDecimal("1000000"), new BigDecimal("75"),
                BaccalaureateMention.TRES_BIEN, null, false, false);
        FinancingPlan plan = new FinancingPlan(
                "etu-1", new BigDecimal("100000.00"), new BigDecimal("168000.00"),
                new BigDecimal("0.00"), new BigDecimal("168000.00"), ApprovalStatus.VALIDATION_MANUELLE_REQUISE);

        when(mapper.toDomain(requestDto)).thenReturn(domainApplication);
        when(engine.calculate(domainApplication)).thenReturn(plan);

        EdTechController controller = new EdTechController(engine, mapper, repository);
        controller.calculerPlanFinancement(requestDto);

        verify(repository, times(1)).save(plan);
    }
}
