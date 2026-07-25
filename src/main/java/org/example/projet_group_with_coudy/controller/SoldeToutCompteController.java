package org.example.projet_group_with_coudy.controller;

import org.example.projet_group_with_coudy.api.SoldeToutCompteApi;
import org.example.projet_group_with_coudy.dto.DossierDepart;
import org.example.projet_group_with_coudy.dto.SoldeToutCompte;
import org.example.projet_group_with_coudy.engine.SoldeToutCompteEngine;
import org.example.projet_group_with_coudy.mapper.SoldeToutCompteMapper;
import org.example.projet_group_with_coudy.model.EmployeeDepartureFile;
import org.example.projet_group_with_coudy.model.SeveranceStatement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SoldeToutCompteController implements SoldeToutCompteApi {

    private final SoldeToutCompteEngine engine;
    private final SoldeToutCompteMapper mapper;

    public SoldeToutCompteController(SoldeToutCompteEngine engine, SoldeToutCompteMapper mapper) {
        this.engine = engine;
        this.mapper = mapper;
    }

    @Override
    public ResponseEntity<SoldeToutCompte> calculerSoldeToutCompte(DossierDepart dossierDepart) {
        EmployeeDepartureFile file = mapper.toDomain(dossierDepart);
        SeveranceStatement statement = engine.calculate(file);
        return ResponseEntity.ok(mapper.toDto(statement));
    }
}
