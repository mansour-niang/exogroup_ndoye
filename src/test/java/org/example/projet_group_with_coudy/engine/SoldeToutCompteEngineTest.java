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
}
