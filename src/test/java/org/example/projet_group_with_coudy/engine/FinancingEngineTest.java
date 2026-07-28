package org.example.projet_group_with_coudy.engine;

import org.example.projet_group_with_coudy.model.BaccalaureateMention;
import org.example.projet_group_with_coudy.model.FinancingPlan;
import org.example.projet_group_with_coudy.model.StudentApplication;
import org.example.projet_group_with_coudy.model.StudyCycle;
import org.example.projet_group_with_coudy.port.HousingAidPort;
import org.example.projet_group_with_coudy.port.TreasuryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FinancingEngineTest {

    @Mock
    private HousingAidPort housingAidPort;

    @Mock
    private TreasuryPort treasuryPort;

    @BeforeEach
    void aucuneAideAuLogementParDefaut() {
        // Le port est toujours interroge (Besoin 4) ; par defaut, aucune aide percue.
        // Les tests du Besoin 4 surchargent ce stub pour un studentId precis.
        when(housingAidPort.getHousingAidAmount(anyString())).thenReturn(BigDecimal.ZERO);
    }

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

    @Test
    void verse_une_bourse_de_mobilite_avec_coefficient_2_si_revenu_sous_le_seuil() {
        // Besoin 2 : distance 75km > 50km -> bourse de base ; revenu <= seuil (1200000) -> coefficient 2.0
        // 60000 * 2.0 = 120000.00
        FinancingEngine engine = new FinancingEngine(housingAidPort, treasuryPort);
        StudentApplication application = new StudentApplication(
                "etu-4", StudyCycle.LICENCE, new BigDecimal("1000000"), new BigDecimal("75"),
                BaccalaureateMention.PASSABLE, null, false, false);

        FinancingPlan plan = engine.calculate(application);

        assertEquals(new BigDecimal("120000.00"), plan.grossMonthlyScholarship());
    }

    @Test
    void verse_une_bourse_de_mobilite_avec_coefficient_1_5_entre_1_et_2_fois_le_seuil() {
        // revenu (2000000) entre 1x et 2x le seuil (1200000-2400000) -> coefficient 1.5 ; 60000*1.5=90000.00
        FinancingEngine engine = new FinancingEngine(housingAidPort, treasuryPort);
        StudentApplication application = new StudentApplication(
                "etu-5", StudyCycle.LICENCE, new BigDecimal("2000000"), new BigDecimal("75"),
                BaccalaureateMention.PASSABLE, null, false, false);

        FinancingPlan plan = engine.calculate(application);

        assertEquals(new BigDecimal("90000.00"), plan.grossMonthlyScholarship());
    }

    @Test
    void verse_une_bourse_de_mobilite_avec_coefficient_1_entre_2_et_4_fois_le_seuil() {
        // revenu (4000000) entre 2x et 4x le seuil (2400000-4800000) -> coefficient 1.0 ; 60000*1.0=60000.00
        FinancingEngine engine = new FinancingEngine(housingAidPort, treasuryPort);
        StudentApplication application = new StudentApplication(
                "etu-6", StudyCycle.LICENCE, new BigDecimal("4000000"), new BigDecimal("75"),
                BaccalaureateMention.PASSABLE, null, false, false);

        FinancingPlan plan = engine.calculate(application);

        assertEquals(new BigDecimal("60000.00"), plan.grossMonthlyScholarship());
    }

    @Test
    void verse_une_bourse_de_mobilite_avec_coefficient_0_5_au_dela_de_4_fois_le_seuil() {
        // revenu (5000000) > 4x le seuil (4800000) -> coefficient 0.5 ; 60000*0.5=30000.00
        FinancingEngine engine = new FinancingEngine(housingAidPort, treasuryPort);
        StudentApplication application = new StudentApplication(
                "etu-7", StudyCycle.LICENCE, new BigDecimal("5000000"), new BigDecimal("75"),
                BaccalaureateMention.PASSABLE, null, false, false);

        FinancingPlan plan = engine.calculate(application);

        assertEquals(new BigDecimal("30000.00"), plan.grossMonthlyScholarship());
    }

    @Test
    void ne_verse_pas_de_bourse_de_mobilite_si_distance_inferieure_ou_egale_a_50km() {
        FinancingEngine engine = new FinancingEngine(housingAidPort, treasuryPort);
        StudentApplication application = new StudentApplication(
                "etu-8", StudyCycle.LICENCE, new BigDecimal("1000000"), new BigDecimal("50"),
                BaccalaureateMention.PASSABLE, null, false, false);

        FinancingPlan plan = engine.calculate(application);

        assertEquals(new BigDecimal("0.00"), plan.grossMonthlyScholarship());
    }

    @Test
    void majore_la_bourse_de_40_pourcent_avec_la_mention_tres_bien() {
        // base = 60000*2.0 = 120000.00 ; excellence (mention TRES_BIEN) -> 120000*1.40 = 168000.00
        FinancingEngine engine = new FinancingEngine(housingAidPort, treasuryPort);
        StudentApplication application = new StudentApplication(
                "etu-9", StudyCycle.LICENCE, new BigDecimal("1000000"), new BigDecimal("75"),
                BaccalaureateMention.TRES_BIEN, null, false, false);

        FinancingPlan plan = engine.calculate(application);

        assertEquals(new BigDecimal("168000.00"), plan.grossMonthlyScholarship());
    }

    @Test
    void majore_la_bourse_de_40_pourcent_avec_une_moyenne_precedente_superieure_a_16() {
        // meme calcul, excellence via moyenne (17 > 16) au lieu de la mention
        FinancingEngine engine = new FinancingEngine(housingAidPort, treasuryPort);
        StudentApplication application = new StudentApplication(
                "etu-10", StudyCycle.LICENCE, new BigDecimal("1000000"), new BigDecimal("75"),
                BaccalaureateMention.PASSABLE, new BigDecimal("17"), false, false);

        FinancingPlan plan = engine.calculate(application);

        assertEquals(new BigDecimal("168000.00"), plan.grossMonthlyScholarship());
    }

    @Test
    void ne_majore_pas_la_bourse_si_ni_mention_tres_bien_ni_moyenne_superieure_a_16() {
        FinancingEngine engine = new FinancingEngine(housingAidPort, treasuryPort);
        StudentApplication application = new StudentApplication(
                "etu-11", StudyCycle.LICENCE, new BigDecimal("1000000"), new BigDecimal("75"),
                BaccalaureateMention.PASSABLE, new BigDecimal("15"), false, false);

        FinancingPlan plan = engine.calculate(application);

        assertEquals(new BigDecimal("120000.00"), plan.grossMonthlyScholarship());
    }

    @Test
    void suspend_integralement_la_bourse_en_cas_de_redoublement_non_justifie_medicalement() {
        // Meme avec une mention TRES_BIEN, le redoublement non justifie suspend tout
        FinancingEngine engine = new FinancingEngine(housingAidPort, treasuryPort);
        StudentApplication application = new StudentApplication(
                "etu-12", StudyCycle.LICENCE, new BigDecimal("1000000"), new BigDecimal("75"),
                BaccalaureateMention.TRES_BIEN, null, true, false);

        FinancingPlan plan = engine.calculate(application);

        assertEquals(new BigDecimal("0.00"), plan.grossMonthlyScholarship());
    }

    @Test
    void ne_suspend_pas_la_bourse_si_le_redoublement_est_justifie_medicalement() {
        FinancingEngine engine = new FinancingEngine(housingAidPort, treasuryPort);
        StudentApplication application = new StudentApplication(
                "etu-13", StudyCycle.LICENCE, new BigDecimal("1000000"), new BigDecimal("75"),
                BaccalaureateMention.PASSABLE, null, true, true);

        FinancingPlan plan = engine.calculate(application);

        assertEquals(new BigDecimal("120000.00"), plan.grossMonthlyScholarship());
    }

    @Test
    void deduit_le_montant_exact_de_l_aide_au_logement_de_la_bourse() {
        // base = 60000*2.0 = 120000.00 ; aide au logement 15000.00 -> net = 105000.00
        when(housingAidPort.getHousingAidAmount("etu-14")).thenReturn(new BigDecimal("15000.00"));

        FinancingEngine engine = new FinancingEngine(housingAidPort, treasuryPort);
        StudentApplication application = new StudentApplication(
                "etu-14", StudyCycle.LICENCE, new BigDecimal("1000000"), new BigDecimal("75"),
                BaccalaureateMention.PASSABLE, null, false, false);

        FinancingPlan plan = engine.calculate(application);

        assertEquals(new BigDecimal("15000.00"), plan.housingAidDeduction());
        assertEquals(new BigDecimal("105000.00"), plan.monthlyScholarship());
    }

    @Test
    void ne_deduit_rien_si_l_etudiant_ne_percoit_aucune_aide_au_logement() {
        when(housingAidPort.getHousingAidAmount("etu-15")).thenReturn(BigDecimal.ZERO);

        FinancingEngine engine = new FinancingEngine(housingAidPort, treasuryPort);
        StudentApplication application = new StudentApplication(
                "etu-15", StudyCycle.LICENCE, new BigDecimal("1000000"), new BigDecimal("75"),
                BaccalaureateMention.PASSABLE, null, false, false);

        FinancingPlan plan = engine.calculate(application);

        assertEquals(new BigDecimal("120000.00"), plan.monthlyScholarship());
    }

    @Test
    void plafonne_la_bourse_nette_a_zero_si_l_aide_au_logement_depasse_la_bourse_brute() {
        // gross = 120000.00 ; aide 130000.00 > gross -> plancher a 0
        when(housingAidPort.getHousingAidAmount("etu-16")).thenReturn(new BigDecimal("130000.00"));

        FinancingEngine engine = new FinancingEngine(housingAidPort, treasuryPort);
        StudentApplication application = new StudentApplication(
                "etu-16", StudyCycle.LICENCE, new BigDecimal("1000000"), new BigDecimal("75"),
                BaccalaureateMention.PASSABLE, null, false, false);

        FinancingPlan plan = engine.calculate(application);

        assertEquals(new BigDecimal("0.00"), plan.monthlyScholarship());
    }
}
