package org.example.projet_group_with_coudy.controller;

import org.example.projet_group_with_coudy.api.AgriTechApi;
import org.example.projet_group_with_coudy.dto.AllocationSubvention;
import org.example.projet_group_with_coudy.dto.DeclarationExploitation;
import org.example.projet_group_with_coudy.engine.AgriculturalSubsidyEngine;
import org.example.projet_group_with_coudy.mapper.AgriTechMapper;
import org.example.projet_group_with_coudy.model.FarmDeclaration;
import org.example.projet_group_with_coudy.model.SubsidyAllocation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AgriTechController implements AgriTechApi {

    private final AgriculturalSubsidyEngine engine;
    private final AgriTechMapper mapper;

    public AgriTechController(AgriculturalSubsidyEngine engine, AgriTechMapper mapper) {
        this.engine = engine;
        this.mapper = mapper;
    }

    @Override
    public ResponseEntity<AllocationSubvention> calculerAllocationSubvention(DeclarationExploitation declarationExploitation) {
        FarmDeclaration declaration = mapper.toDomain(declarationExploitation);
        SubsidyAllocation allocation = engine.calculate(declaration);
        return ResponseEntity.ok(mapper.toDto(allocation));
    }
}
