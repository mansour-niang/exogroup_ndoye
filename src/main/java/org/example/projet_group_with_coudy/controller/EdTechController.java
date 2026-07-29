package org.example.projet_group_with_coudy.controller;

import org.example.projet_group_with_coudy.api.EdTechApi;
import org.example.projet_group_with_coudy.dto.DossierEtudiant;
import org.example.projet_group_with_coudy.dto.PlanFinancement;
import org.example.projet_group_with_coudy.engine.FinancingEngine;
import org.example.projet_group_with_coudy.mapper.EdTechMapper;
import org.example.projet_group_with_coudy.model.FinancingPlan;
import org.example.projet_group_with_coudy.model.StudentApplication;
import org.example.projet_group_with_coudy.repository.FinancingPlanRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EdTechController implements EdTechApi {

    private final FinancingEngine engine;
    private final EdTechMapper mapper;
    private final FinancingPlanRepository repository;

    public EdTechController(FinancingEngine engine, EdTechMapper mapper, FinancingPlanRepository repository) {
        this.engine = engine;
        this.mapper = mapper;
        this.repository = repository;
    }

    @Override
    public ResponseEntity<PlanFinancement> calculerPlanFinancement(DossierEtudiant dossierEtudiant) {
        StudentApplication application = mapper.toDomain(dossierEtudiant);
        FinancingPlan plan = engine.calculate(application);
        repository.save(plan);
        return ResponseEntity.ok(mapper.toDto(plan));
    }
}
