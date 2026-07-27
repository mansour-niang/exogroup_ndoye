package org.example.projet_group_with_coudy.mapper;

import org.example.projet_group_with_coudy.dto.AllocationSubvention;
import org.example.projet_group_with_coudy.dto.DeclarationExploitation;
import org.example.projet_group_with_coudy.dto.StatutAllocation;
import org.example.projet_group_with_coudy.dto.TypeCulture;
import org.example.projet_group_with_coudy.model.AllocationStatus;
import org.example.projet_group_with_coudy.model.CropType;
import org.example.projet_group_with_coudy.model.FarmDeclaration;
import org.example.projet_group_with_coudy.model.SubsidyAllocation;
import org.springframework.stereotype.Component;

@Component
public class AgriTechMapper {

    public FarmDeclaration toDomain(DeclarationExploitation dto) {
        return new FarmDeclaration(
                dto.getFarmId(),
                dto.getHectares(),
                CropType.valueOf(dto.getTypeCulture().name()),
                dto.getCertificationBiologique(),
                dto.getRendementDeclareParHectare(),
                dto.getLocalisation());
    }

    public AllocationSubvention toDto(SubsidyAllocation statement) {
        return new AllocationSubvention(
                statement.farmId(),
                statement.baseSubsidy(),
                statement.ecologicalBonus(),
                statement.underproductionPenalty(),
                statement.emergencyFund(),
                statement.finalAmount(),
                StatutAllocation.valueOf(statement.allocationStatus().name()));
    }
}
