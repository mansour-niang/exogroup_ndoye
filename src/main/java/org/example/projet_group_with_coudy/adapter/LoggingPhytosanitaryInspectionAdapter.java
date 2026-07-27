package org.example.projet_group_with_coudy.adapter;

import org.example.projet_group_with_coudy.port.PhytosanitaryInspectionPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Doublure temporaire : journalise la notification au lieu d'appeler le
 * vrai systeme de l'Inspection Phytosanitaire. A remplacer une fois les
 * details d'integration (URL, protocole) connus.
 */
@Component
public class LoggingPhytosanitaryInspectionAdapter implements PhytosanitaryInspectionPort {

    private static final Logger LOG = LoggerFactory.getLogger(LoggingPhytosanitaryInspectionAdapter.class);

    @Override
    public void reportForInspection(String farmId, BigDecimal finalAmount) {
        LOG.warn("Signalement Inspection Phytosanitaire (stub) : exploitation={}, montant final={}", farmId, finalAmount);
    }
}
