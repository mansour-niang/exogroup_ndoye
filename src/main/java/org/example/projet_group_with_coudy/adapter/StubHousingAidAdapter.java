package org.example.projet_group_with_coudy.adapter;

import org.example.projet_group_with_coudy.port.HousingAidPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Doublure temporaire : aucune donnee reelle n'est interrogee aupres du
 * registre national des aides sociales. A remplacer par un vrai client une
 * fois les details d'integration connus.
 */
@Component
public class StubHousingAidAdapter implements HousingAidPort {

    private static final Logger LOG = LoggerFactory.getLogger(StubHousingAidAdapter.class);

    @Override
    public BigDecimal getHousingAidAmount(String studentId) {
        LOG.warn("StubHousingAidAdapter utilise : aucune aide au logement detectee pour l'etudiant {}", studentId);
        return BigDecimal.ZERO;
    }
}
