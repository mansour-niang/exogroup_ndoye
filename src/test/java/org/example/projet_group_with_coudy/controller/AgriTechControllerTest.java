package org.example.projet_group_with_coudy.controller;

import org.example.projet_group_with_coudy.dto.AllocationSubvention;
import org.example.projet_group_with_coudy.dto.DeclarationExploitation;
import org.example.projet_group_with_coudy.dto.StatutAllocation;
import org.example.projet_group_with_coudy.dto.TypeCulture;
import org.example.projet_group_with_coudy.engine.AgriculturalSubsidyEngine;
import org.example.projet_group_with_coudy.mapper.AgriTechMapper;
import org.example.projet_group_with_coudy.model.AllocationStatus;
import org.example.projet_group_with_coudy.model.CropType;
import org.example.projet_group_with_coudy.model.FarmDeclaration;
import org.example.projet_group_with_coudy.model.SubsidyAllocation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AgriTechControllerTest {

    @Mock
    private AgriculturalSubsidyEngine engine;

    @Mock
    private AgriTechMapper mapper;

    @Test
    void delegue_la_conversion_au_mapper_et_le_calcul_au_moteur() {
        DeclarationExploitation requestDto = new DeclarationExploitation(
                "farm-1", new BigDecimal("10"), TypeCulture.MIL, true, new BigDecimal("600"), "Kaffrine");
        FarmDeclaration domainDeclaration = new FarmDeclaration(
                "farm-1", new BigDecimal("10"), CropType.MIL, true, new BigDecimal("600"), "Kaffrine");
        SubsidyAllocation allocation = new SubsidyAllocation(
                "farm-1", new BigDecimal("1000000.00"), new BigDecimal("150000.00"),
                new BigDecimal("0.00"), new BigDecimal("0.00"),
                new BigDecimal("1150000.00"), AllocationStatus.ALLOUE);
        AllocationSubvention responseDto = new AllocationSubvention(
                "farm-1", new BigDecimal("1000000.00"), new BigDecimal("150000.00"),
                new BigDecimal("0.00"), new BigDecimal("0.00"),
                new BigDecimal("1150000.00"), StatutAllocation.ALLOUE);

        when(mapper.toDomain(requestDto)).thenReturn(domainDeclaration);
        when(engine.calculate(domainDeclaration)).thenReturn(allocation);
        when(mapper.toDto(allocation)).thenReturn(responseDto);

        AgriTechController controller = new AgriTechController(engine, mapper);
        ResponseEntity<AllocationSubvention> response = controller.calculerAllocationSubvention(requestDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(responseDto, response.getBody());
    }
}
