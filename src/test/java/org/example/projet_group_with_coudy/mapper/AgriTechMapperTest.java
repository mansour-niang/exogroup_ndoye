package org.example.projet_group_with_coudy.mapper;

import org.example.projet_group_with_coudy.dto.AllocationSubvention;
import org.example.projet_group_with_coudy.dto.DeclarationExploitation;
import org.example.projet_group_with_coudy.dto.StatutAllocation;
import org.example.projet_group_with_coudy.dto.TypeCulture;
import org.example.projet_group_with_coudy.model.AllocationStatus;
import org.example.projet_group_with_coudy.model.CropType;
import org.example.projet_group_with_coudy.model.FarmDeclaration;
import org.example.projet_group_with_coudy.model.SubsidyAllocation;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AgriTechMapperTest {

    private final AgriTechMapper mapper = new AgriTechMapper();

    @Test
    void convertit_le_dto_de_requete_en_modele_de_domaine() {
        DeclarationExploitation dto = new DeclarationExploitation(
                "farm-1", new BigDecimal("12.5"), TypeCulture.MIL, true, new BigDecimal("450"), "Kaffrine");

        FarmDeclaration domain = mapper.toDomain(dto);

        assertEquals(new FarmDeclaration(
                "farm-1", new BigDecimal("12.5"), CropType.MIL, true, new BigDecimal("450"), "Kaffrine"), domain);
    }

    @Test
    void convertit_le_modele_de_domaine_en_dto_de_reponse() {
        SubsidyAllocation domain = new SubsidyAllocation(
                "farm-1", new BigDecimal("1000000.00"), new BigDecimal("150000.00"),
                new BigDecimal("0.00"), new BigDecimal("0.00"),
                new BigDecimal("1150000.00"), AllocationStatus.ALLOUE);

        AllocationSubvention dto = mapper.toDto(domain);

        assertEquals(new AllocationSubvention(
                "farm-1", new BigDecimal("1000000.00"), new BigDecimal("150000.00"),
                new BigDecimal("0.00"), new BigDecimal("0.00"),
                new BigDecimal("1150000.00"), StatutAllocation.ALLOUE), dto);
    }
}
