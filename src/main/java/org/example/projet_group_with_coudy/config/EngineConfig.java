package org.example.projet_group_with_coudy.config;

import org.example.projet_group_with_coudy.engine.SoldeToutCompteEngine;
import org.example.projet_group_with_coudy.port.LaborInspectionPort;
import org.example.projet_group_with_coudy.port.TaxAdministrationPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Le moteur ({@link SoldeToutCompteEngine}) reste un POJO sans dependance
 * Spring : c'est ici, et seulement ici, qu'il est instancie et connecte aux
 * implementations des ports (via injection par constructeur).
 */
@Configuration
public class EngineConfig {

    @Bean
    public SoldeToutCompteEngine soldeToutCompteEngine(
            TaxAdministrationPort taxAdministrationPort,
            LaborInspectionPort laborInspectionPort) {
        return new SoldeToutCompteEngine(taxAdministrationPort, laborInspectionPort);
    }
}
