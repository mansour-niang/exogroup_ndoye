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
import static org.mockito.Mockito.when;

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

    @Test
    void applique_un_bonus_de_15_pourcent_si_certification_biologique_valide() {
        // Besoin 2 : MIL, 10 ha => base=1000000.00, bonus=15%=150000.00, final=1150000.00
        AgriculturalSubsidyEngine engine = new AgriculturalSubsidyEngine(meteorologyPort, phytosanitaryInspectionPort);
        FarmDeclaration declaration = new FarmDeclaration(
                "farm-3", new BigDecimal("10"), CropType.MIL, true, new BigDecimal("600"), "Kaffrine");

        SubsidyAllocation allocation = engine.calculate(declaration);

        assertEquals(new BigDecimal("150000.00"), allocation.ecologicalBonus());
        assertEquals(new BigDecimal("1150000.00"), allocation.finalAmount());
    }

    @Test
    void ampute_de_50_pourcent_si_rendement_sous_le_seuil_critique_et_pas_de_secheresse() {
        // Besoin 3 : MIL, rendement 400 < seuil critique 500 -> penalite 50% ; pas de secheresse -> pas de fonds d'urgence
        // base = 1000000.00 ; penalite = 500000.00 ; final = 500000.00
        when(meteorologyPort.isSevereDrought("Kaffrine")).thenReturn(false);

        AgriculturalSubsidyEngine engine = new AgriculturalSubsidyEngine(meteorologyPort, phytosanitaryInspectionPort);
        FarmDeclaration declaration = new FarmDeclaration(
                "farm-4", new BigDecimal("10"), CropType.MIL, false, new BigDecimal("400"), "Kaffrine");

        SubsidyAllocation allocation = engine.calculate(declaration);

        assertEquals(new BigDecimal("500000.00"), allocation.underproductionPenalty());
        assertEquals(new BigDecimal("0.00"), allocation.emergencyFund());
        assertEquals(new BigDecimal("500000.00"), allocation.finalAmount());
    }

    @Test
    void annule_la_penalite_et_ajoute_un_fonds_d_urgence_si_secheresse_severe_confirmee() {
        // Meme rendement insuffisant, mais secheresse severe confirmee par l'Agence Nationale de Meteorologie
        // base = 1000000.00 ; penalite = 0 (annulee) ; fonds d'urgence = 500000.00 ; final = 1500000.00
        when(meteorologyPort.isSevereDrought("Kaffrine")).thenReturn(true);

        AgriculturalSubsidyEngine engine = new AgriculturalSubsidyEngine(meteorologyPort, phytosanitaryInspectionPort);
        FarmDeclaration declaration = new FarmDeclaration(
                "farm-5", new BigDecimal("10"), CropType.MIL, false, new BigDecimal("400"), "Kaffrine");

        SubsidyAllocation allocation = engine.calculate(declaration);

        assertEquals(new BigDecimal("0.00"), allocation.underproductionPenalty());
        assertEquals(new BigDecimal("500000.00"), allocation.emergencyFund());
        assertEquals(new BigDecimal("1500000.00"), allocation.finalAmount());
    }
}
