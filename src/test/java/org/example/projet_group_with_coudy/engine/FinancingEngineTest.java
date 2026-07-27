package org.example.projet_group_with_coudy.engine;

import org.example.projet_group_with_coudy.model.BaccalaureateMention;
import org.example.projet_group_with_coudy.model.FinancingPlan;
import org.example.projet_group_with_coudy.model.StudentApplication;
import org.example.projet_group_with_coudy.model.StudyCycle;
import org.example.projet_group_with_coudy.port.HousingAidPort;
import org.example.projet_group_with_coudy.port.TreasuryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class FinancingEngineTest {

    @Mock
    private HousingAidPort housingAidPort;

    @Mock
    private TreasuryPort treasuryPort;

    @Test
    void applique_le_tarif_licence_quand_le_revenu_depasse_le_seuil_de_pauvrete() {
        // Besoin 1 : revenu (2000000) > seuil de pauvrete (1200000) -> frais licence pleins = 100000.00
        // distance (10km) <= 50km -> pas de bourse de mobilite, isole le Besoin 1
        FinancingEngine engine = new FinancingEngine(housingAidPort, treasuryPort);
        StudentApplication application = new StudentApplication(
                "etu-1", StudyCycle.LICENCE, new BigDecimal("2000000"), new BigDecimal("10"),
                BaccalaureateMention.PASSABLE, null, false, false);

        FinancingPlan plan = engine.calculate(application);

        assertEquals(new BigDecimal("100000.00"), plan.tuitionFees());
        assertEquals(new BigDecimal("0.00"), plan.monthlyScholarship());
    }

    @Test
    void applique_le_tarif_doctorat_quand_le_revenu_depasse_le_seuil_de_pauvrete() {
        // Besoin 1 : frais doctorat pleins = 200000.00
        FinancingEngine engine = new FinancingEngine(housingAidPort, treasuryPort);
        StudentApplication application = new StudentApplication(
                "etu-2", StudyCycle.DOCTORAT, new BigDecimal("2000000"), new BigDecimal("10"),
                BaccalaureateMention.PASSABLE, null, false, false);

        FinancingPlan plan = engine.calculate(application);

        assertEquals(new BigDecimal("200000.00"), plan.tuitionFees());
    }

    @Test
    void exonere_totalement_les_frais_quand_le_revenu_est_sous_le_seuil_de_pauvrete() {
        // Besoin 1 : revenu (800000) < seuil de pauvrete (1200000) -> exoneration totale, quel que soit le cycle
        FinancingEngine engine = new FinancingEngine(housingAidPort, treasuryPort);
        StudentApplication application = new StudentApplication(
                "etu-3", StudyCycle.DOCTORAT, new BigDecimal("800000"), new BigDecimal("10"),
                BaccalaureateMention.PASSABLE, null, false, false);

        FinancingPlan plan = engine.calculate(application);

        assertEquals(new BigDecimal("0.00"), plan.tuitionFees());
    }
}
