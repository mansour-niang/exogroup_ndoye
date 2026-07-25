package org.example.projet_group_with_coudy.controller;

import org.example.projet_group_with_coudy.dto.DossierDepart;
import org.example.projet_group_with_coudy.dto.MotifDepart;
import org.example.projet_group_with_coudy.dto.SoldeToutCompte;
import org.example.projet_group_with_coudy.engine.SoldeToutCompteEngine;
import org.example.projet_group_with_coudy.mapper.SoldeToutCompteMapper;
import org.example.projet_group_with_coudy.model.DepartureReason;
import org.example.projet_group_with_coudy.model.EmployeeDepartureFile;
import org.example.projet_group_with_coudy.model.SeveranceStatement;
import org.example.projet_group_with_coudy.repository.SeveranceStatementRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SoldeToutCompteControllerTest {

    @Mock
    private SoldeToutCompteEngine engine;

    @Mock
    private SoldeToutCompteMapper mapper;

    @Mock
    private SeveranceStatementRepository repository;

    @Test
    void delegue_la_conversion_au_mapper_et_le_calcul_au_moteur() {
        DossierDepart requestDto = new DossierDepart(
                "emp-1", LocalDate.of(2020, 1, 1), LocalDate.of(2026, 7, 31),
                MotifDepart.RETRAITE, new BigDecimal("500000"), 5, true);
        EmployeeDepartureFile domainFile = new EmployeeDepartureFile(
                "emp-1", LocalDate.of(2020, 1, 1), LocalDate.of(2026, 7, 31),
                DepartureReason.RETRAITE, new BigDecimal("500000"), 5, true);
        SeveranceStatement statement = new SeveranceStatement(
                "emp-1", new BigDecimal("100000.00"), new BigDecimal("200000.00"),
                new BigDecimal("0.00"), new BigDecimal("300000.00"),
                new BigDecimal("15000.00"), new BigDecimal("285000.00"), false);
        SoldeToutCompte responseDto = new SoldeToutCompte(
                "emp-1", new BigDecimal("100000.00"), new BigDecimal("200000.00"),
                new BigDecimal("0.00"), new BigDecimal("300000.00"),
                new BigDecimal("15000.00"), new BigDecimal("285000.00"), false);

        when(mapper.toDomain(requestDto)).thenReturn(domainFile);
        when(engine.calculate(domainFile)).thenReturn(statement);
        when(mapper.toDto(statement)).thenReturn(responseDto);

        SoldeToutCompteController controller = new SoldeToutCompteController(engine, mapper, repository);
        ResponseEntity<SoldeToutCompte> response = controller.calculerSoldeToutCompte(requestDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(responseDto, response.getBody());
    }

    @Test
    void persiste_le_solde_de_tout_compte_calcule() {
        DossierDepart requestDto = new DossierDepart(
                "emp-1", LocalDate.of(2020, 1, 1), LocalDate.of(2026, 7, 31),
                MotifDepart.RETRAITE, new BigDecimal("500000"), 5, true);
        EmployeeDepartureFile domainFile = new EmployeeDepartureFile(
                "emp-1", LocalDate.of(2020, 1, 1), LocalDate.of(2026, 7, 31),
                DepartureReason.RETRAITE, new BigDecimal("500000"), 5, true);
        SeveranceStatement statement = new SeveranceStatement(
                "emp-1", new BigDecimal("100000.00"), new BigDecimal("200000.00"),
                new BigDecimal("0.00"), new BigDecimal("300000.00"),
                new BigDecimal("15000.00"), new BigDecimal("285000.00"), false);

        when(mapper.toDomain(requestDto)).thenReturn(domainFile);
        when(engine.calculate(domainFile)).thenReturn(statement);

        SoldeToutCompteController controller = new SoldeToutCompteController(engine, mapper, repository);
        controller.calculerSoldeToutCompte(requestDto);

        verify(repository, times(1)).save(statement);
    }
}
