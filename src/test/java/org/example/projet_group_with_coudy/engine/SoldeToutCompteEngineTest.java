package org.example.projet_group_with_coudy.engine;

import org.example.projet_group_with_coudy.model.DepartureReason;
import org.example.projet_group_with_coudy.model.EmployeeDepartureFile;
import org.example.projet_group_with_coudy.model.SeveranceStatement;
import org.example.projet_group_with_coudy.port.LaborInspectionPort;
import org.example.projet_group_with_coudy.port.TaxAdministrationPort;
import org.example.projet_group_with_coudy.port.TaxableAmounts;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SoldeToutCompteEngineTest {

    @Mock
    private TaxAdministrationPort taxAdministrationPort;

    @Mock
    private LaborInspectionPort laborInspectionPort;

    @Test
    void calcule_l_indemnite_de_conges_payes_et_interroge_le_port_fiscal() {
        // Besoin 1 : 630000/21 = 30000 XOF/jour * 10 jours = 300000.00
        // Demission -> pas de prime d'anciennete (Besoin 2), preavis respecte -> pas de penalite (Besoin 3)
        when(taxAdministrationPort.calculateWithholding(
                new TaxableAmounts(new BigDecimal("300000.00"), new BigDecimal("0.00"))))
                .thenReturn(new BigDecimal("15000.00"));

        SoldeToutCompteEngine engine = new SoldeToutCompteEngine(taxAdministrationPort, laborInspectionPort);
        EmployeeDepartureFile file = new EmployeeDepartureFile(
                "emp-1",
                LocalDate.of(2020, 1, 1),
                LocalDate.of(2026, 7, 31),
                DepartureReason.DEMISSION,
                new BigDecimal("630000"),
                10,
                true);

        SeveranceStatement statement = engine.calculate(file);

        assertEquals(new BigDecimal("300000.00"), statement.paidLeaveIndemnity());
        assertEquals(new BigDecimal("0.00"), statement.seniorityBonus());
        assertEquals(new BigDecimal("0.00"), statement.noticeViolationPenalty());
        assertEquals(new BigDecimal("300000.00"), statement.grossAmount());
        assertEquals(new BigDecimal("15000.00"), statement.taxWithholding());
        assertEquals(new BigDecimal("285000.00"), statement.netAmount());
        assertEquals(false, statement.auditFlagged());
    }

    @Test
    void verse_la_prime_d_anciennete_pour_un_depart_a_la_retraite() {
        // Besoin 2 : 7 ans completes -> 5*10% + 2*15% = 80% de 500000 = 400000.00
        // 0 jour de conge restant pour isoler la prime
        when(taxAdministrationPort.calculateWithholding(
                new TaxableAmounts(new BigDecimal("400000.00"), new BigDecimal("400000.00"))))
                .thenReturn(new BigDecimal("20000.00"));

        SoldeToutCompteEngine engine = new SoldeToutCompteEngine(taxAdministrationPort, laborInspectionPort);
        EmployeeDepartureFile file = new EmployeeDepartureFile(
                "emp-2",
                LocalDate.of(2019, 1, 15),
                LocalDate.of(2026, 1, 15),
                DepartureReason.RETRAITE,
                new BigDecimal("500000"),
                0,
                true);

        SeveranceStatement statement = engine.calculate(file);

        assertEquals(new BigDecimal("0.00"), statement.paidLeaveIndemnity());
        assertEquals(new BigDecimal("400000.00"), statement.seniorityBonus());
        assertEquals(new BigDecimal("400000.00"), statement.grossAmount());
        assertEquals(new BigDecimal("20000.00"), statement.taxWithholding());
        assertEquals(new BigDecimal("380000.00"), statement.netAmount());
    }

    @Test
    void ne_verse_pas_de_prime_d_anciennete_en_cas_de_licenciement_pour_faute_grave() {
        // Besoin 2 (exception) : 10 ans d'anciennete mais faute grave -> prime = 0
        when(taxAdministrationPort.calculateWithholding(
                new TaxableAmounts(new BigDecimal("0.00"), new BigDecimal("0.00"))))
                .thenReturn(new BigDecimal("0.00"));

        SoldeToutCompteEngine engine = new SoldeToutCompteEngine(taxAdministrationPort, laborInspectionPort);
        EmployeeDepartureFile file = new EmployeeDepartureFile(
                "emp-3",
                LocalDate.of(2016, 1, 15),
                LocalDate.of(2026, 1, 15),
                DepartureReason.LICENCIEMENT_FAUTE_GRAVE,
                new BigDecimal("500000"),
                0,
                true);

        SeveranceStatement statement = engine.calculate(file);

        assertEquals(new BigDecimal("0.00"), statement.seniorityBonus());
    }

    @Test
    void retient_un_mois_de_salaire_si_le_preavis_n_est_pas_respecte_et_le_solde_peut_devenir_negatif() {
        // Besoin 3 : demission sans respect du preavis -> penalite = 1 mois de salaire de base (630000)
        // indemnite = 30000/j * 2j = 60000.00 ; net avant penalite = 60000-3000=57000 ; net final = 57000-630000 = -573000.00
        when(taxAdministrationPort.calculateWithholding(
                new TaxableAmounts(new BigDecimal("60000.00"), new BigDecimal("0.00"))))
                .thenReturn(new BigDecimal("3000.00"));

        SoldeToutCompteEngine engine = new SoldeToutCompteEngine(taxAdministrationPort, laborInspectionPort);
        EmployeeDepartureFile file = new EmployeeDepartureFile(
                "emp-4",
                LocalDate.of(2023, 1, 1),
                LocalDate.of(2026, 7, 31),
                DepartureReason.DEMISSION,
                new BigDecimal("630000"),
                2,
                false);

        SeveranceStatement statement = engine.calculate(file);

        assertEquals(new BigDecimal("630000.00"), statement.noticeViolationPenalty());
        assertEquals(new BigDecimal("-573000.00"), statement.netAmount());
    }

    @Test
    void notifie_l_inspection_du_travail_si_le_montant_net_depasse_strictement_30_millions() {
        // 10 ans -> 125% de 30000000 = 37500000.00 de prime, indemnite=0, tax mock=2000000.00
        // net = 37500000 - 2000000 = 35500000.00 (> 30000000)
        when(taxAdministrationPort.calculateWithholding(
                new TaxableAmounts(new BigDecimal("37500000.00"), new BigDecimal("37500000.00"))))
                .thenReturn(new BigDecimal("2000000.00"));

        SoldeToutCompteEngine engine = new SoldeToutCompteEngine(taxAdministrationPort, laborInspectionPort);
        EmployeeDepartureFile file = new EmployeeDepartureFile(
                "emp-5",
                LocalDate.of(2016, 1, 15),
                LocalDate.of(2026, 1, 15),
                DepartureReason.RETRAITE,
                new BigDecimal("30000000"),
                0,
                true);

        SeveranceStatement statement = engine.calculate(file);

        assertEquals(new BigDecimal("35500000.00"), statement.netAmount());
        assertEquals(true, statement.auditFlagged());
        verify(laborInspectionPort, times(1))
                .reportSeveranceForAudit("emp-5", new BigDecimal("35500000.00"));
    }

    @Test
    void ne_notifie_pas_l_inspection_du_travail_si_le_montant_net_est_exactement_30_millions() {
        // 10 ans -> 125% de 24000000 = 30000000.00 de prime, indemnite=0, tax mock=0.00 -> net = 30000000.00 pile
        when(taxAdministrationPort.calculateWithholding(
                new TaxableAmounts(new BigDecimal("30000000.00"), new BigDecimal("30000000.00"))))
                .thenReturn(new BigDecimal("0.00"));

        SoldeToutCompteEngine engine = new SoldeToutCompteEngine(taxAdministrationPort, laborInspectionPort);
        EmployeeDepartureFile file = new EmployeeDepartureFile(
                "emp-6",
                LocalDate.of(2016, 1, 15),
                LocalDate.of(2026, 1, 15),
                DepartureReason.RETRAITE,
                new BigDecimal("24000000"),
                0,
                true);

        SeveranceStatement statement = engine.calculate(file);

        assertEquals(new BigDecimal("30000000.00"), statement.netAmount());
        assertEquals(false, statement.auditFlagged());
        verify(laborInspectionPort, never()).reportSeveranceForAudit(anyString(), any());
    }
}
