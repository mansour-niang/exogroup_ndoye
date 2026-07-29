package org.example.projet_group_with_coudy.adapter;

import org.example.projet_group_with_coudy.port.TreasuryPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Doublure temporaire : journalise la demande de derogation exceptionnelle
 * au lieu d'appeler le vrai systeme du Tresor Public. A remplacer une fois
 * les details d'integration (URL, protocole) connus.
 */
@Component
public class LoggingTreasuryAdapter implements TreasuryPort {

    private static final Logger LOG = LoggerFactory.getLogger(LoggingTreasuryAdapter.class);

    @Override
    public void requestExceptionalWaiver(String studentId, BigDecimal monthlyScholarship) {
        LOG.warn("Demande de derogation exceptionnelle (stub) : etudiant={}, bourse mensuelle={}",
                studentId, monthlyScholarship);
    }
}
