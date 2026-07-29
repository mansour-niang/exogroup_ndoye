package org.example.projet_group_with_coudy.config;

import org.example.projet_group_with_coudy.engine.FinancingEngine;
import org.example.projet_group_with_coudy.port.HousingAidPort;
import org.example.projet_group_with_coudy.port.TreasuryPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Le moteur ({@link FinancingEngine}) reste un POJO sans dependance Spring :
 * c'est ici, et seulement ici, qu'il est instancie et connecte aux
 * implementations des ports (via injection par constructeur).
 */
@Configuration
public class EngineConfig {

    @Bean
    public FinancingEngine financingEngine(HousingAidPort housingAidPort, TreasuryPort treasuryPort) {
        return new FinancingEngine(housingAidPort, treasuryPort);
    }
}
