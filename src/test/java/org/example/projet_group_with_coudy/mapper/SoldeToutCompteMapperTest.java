package org.example.projet_group_with_coudy.mapper;

import org.example.projet_group_with_coudy.dto.DossierDepart;
import org.example.projet_group_with_coudy.dto.MotifDepart;
import org.example.projet_group_with_coudy.dto.SoldeToutCompte;
import org.example.projet_group_with_coudy.model.DepartureReason;
import org.example.projet_group_with_coudy.model.EmployeeDepartureFile;
import org.example.projet_group_with_coudy.model.SeveranceStatement;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SoldeToutCompteMapperTest {

    private final SoldeToutCompteMapper mapper = new SoldeToutCompteMapper();

    @Test
    void convertit_le_dossier_depart_dto_en_modele_de_domaine() {
        DossierDepart dto = new DossierDepart(
                "emp-1",
                LocalDate.of(2020, 1, 1),
                LocalDate.of(2026, 7, 31),
                MotifDepart.LICENCIEMENT_ECONOMIQUE,
                new BigDecimal("630000"),
                10,
                true);

        EmployeeDepartureFile domain = mapper.toDomain(dto);

        assertEquals("emp-1", domain.employeeId());
        assertEquals(LocalDate.of(2020, 1, 1), domain.hireDate());
        assertEquals(LocalDate.of(2026, 7, 31), domain.departureDate());
        assertEquals(DepartureReason.LICENCIEMENT_ECONOMIQUE, domain.reason());
        assertEquals(new BigDecimal("630000"), domain.baseMonthlySalary());
        assertEquals(10, domain.remainingLeaveDays());
        assertEquals(true, domain.noticePeriodRespected());
    }

    @Test
    void convertit_le_resultat_du_moteur_en_dto_de_reponse() {
        SeveranceStatement statement = new SeveranceStatement(
                "emp-1",
                new BigDecimal("300000.00"),
                new BigDecimal("400000.00"),
                new BigDecimal("0.00"),
                new BigDecimal("700000.00"),
                new BigDecimal("35000.00"),
                new BigDecimal("665000.00"),
                false);

        SoldeToutCompte dto = mapper.toDto(statement);

        assertEquals("emp-1", dto.getEmployeeId());
        assertEquals(new BigDecimal("300000.00"), dto.getMontantIndemniteCongesPayes());
        assertEquals(new BigDecimal("400000.00"), dto.getMontantPrimeAnciennete());
        assertEquals(new BigDecimal("0.00"), dto.getMontantPenalitePreavis());
        assertEquals(new BigDecimal("700000.00"), dto.getMontantBrut());
        assertEquals(new BigDecimal("35000.00"), dto.getMontantDeductionsFiscales());
        assertEquals(new BigDecimal("665000.00"), dto.getMontantNet());
        assertEquals(false, dto.getIndicateurAudit());
    }
}
