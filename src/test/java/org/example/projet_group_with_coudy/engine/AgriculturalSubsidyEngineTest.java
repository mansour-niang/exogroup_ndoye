package org.example.projet_group_with_coudy.engine;

import org.example.projet_group_with_coudy.model.CropType;
import org.example.projet_group_with_coudy.model.FarmDeclaration;
import org.example.projet_group_with_coudy.model.SubsidyAllocation;
import org.example.projet_group_with_coudy.port.MeteorologyPort;
import org.example.projet_group_with_coudy.port.PhytosanitaryInspectionPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class AgriculturalSubsidyEngineTest {

    @Mock
    private MeteorologyPort meteorologyPort;

    @Mock
    private PhytosanitaryInspectionPort phytosanitaryInspectionPort;

    @Test
    void applique_le_taux_vivrier_deux_fois_superieur_au_taux_export() {
        // Besoin 1 : MIL (vivriere) = 100000 XOF/ha, 10 ha => 1000000.00
        // Rendement (600) > seuil critique du MIL (500) -> pas de penalite ; pas de secheresse -> pas de fonds d'urgence
        AgriculturalSubsidyEngine engine = new AgriculturalSubsidyEngine(meteorologyPort, phytosanitaryInspectionPort);
        FarmDeclaration declaration = new FarmDeclaration(
                "farm-1", new BigDecimal("10"), CropType.MIL, false, new BigDecimal("600"), "Kaffrine");

        SubsidyAllocation allocation = engine.calculate(declaration);

        assertEquals(new BigDecimal("1000000.00"), allocation.baseSubsidy());
        assertEquals(new BigDecimal("1000000.00"), allocation.finalAmount());
    }

    @Test
    void applique_le_taux_export_deux_fois_inferieur_au_taux_vivrier() {
        // Besoin 1 : ARACHIDE (export) = 50000 XOF/ha, 10 ha => 500000.00
        AgriculturalSubsidyEngine engine = new AgriculturalSubsidyEngine(meteorologyPort, phytosanitaryInspectionPort);
        FarmDeclaration declaration = new FarmDeclaration(
                "farm-2", new BigDecimal("10"), CropType.ARACHIDE, false, new BigDecimal("900"), "Kaffrine");

        SubsidyAllocation allocation = engine.calculate(declaration);

        assertEquals(new BigDecimal("500000.00"), allocation.baseSubsidy());
        assertEquals(new BigDecimal("500000.00"), allocation.finalAmount());
    }
}
