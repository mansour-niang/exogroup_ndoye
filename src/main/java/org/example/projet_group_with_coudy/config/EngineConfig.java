package org.example.projet_group_with_coudy.config;

import org.example.projet_group_with_coudy.engine.AgriculturalSubsidyEngine;
import org.example.projet_group_with_coudy.port.MeteorologyPort;
import org.example.projet_group_with_coudy.port.PhytosanitaryInspectionPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Le moteur ({@link AgriculturalSubsidyEngine}) reste un POJO sans
 * dependance Spring : c'est ici, et seulement ici, qu'il est instancie et
 * connecte aux implementations des ports (via injection par constructeur).
 */
@Configuration
public class EngineConfig {

    @Bean
    public AgriculturalSubsidyEngine agriculturalSubsidyEngine(
            MeteorologyPort meteorologyPort, PhytosanitaryInspectionPort phytosanitaryInspectionPort) {
        return new AgriculturalSubsidyEngine(meteorologyPort, phytosanitaryInspectionPort);
    }
}
