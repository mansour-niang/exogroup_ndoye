package org.example.projet_group_with_coudy.adapter;

import org.example.projet_group_with_coudy.port.MeteorologyPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Doublure temporaire : aucune donnee meteorologique reelle n'est
 * interrogee. A remplacer par un vrai client vers l'Agence Nationale de
 * Meteorologie une fois les details d'integration connus.
 */
@Component
public class StubMeteorologyAdapter implements MeteorologyPort {

    private static final Logger LOG = LoggerFactory.getLogger(StubMeteorologyAdapter.class);

    @Override
    public boolean isSevereDrought(String location) {
        LOG.warn("StubMeteorologyAdapter utilise : aucune secheresse detectee pour la localisation {}", location);
        return false;
    }
}
