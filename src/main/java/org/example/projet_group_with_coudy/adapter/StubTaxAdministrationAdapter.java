package org.example.projet_group_with_coudy.adapter;

import org.example.projet_group_with_coudy.port.TaxAdministrationPort;
import org.example.projet_group_with_coudy.port.TaxableAmounts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Implementation temporaire de {@link TaxAdministrationPort}, en attendant le
 * branchement au systeme reel de l'administration fiscale. Ne calcule aucun
 * impot (retourne toujours zero) : la formule de calcul de la retenue fiscale
 * n'est pas connue et ne doit pas etre devinee par ce module.
 */
@Component
public class StubTaxAdministrationAdapter implements TaxAdministrationPort {

    private static final Logger log = LoggerFactory.getLogger(StubTaxAdministrationAdapter.class);

    @Override
    public BigDecimal calculateWithholding(TaxableAmounts taxableAmounts) {
        log.warn("StubTaxAdministrationAdapter utilise : aucune retenue fiscale reelle appliquee ({})",
                taxableAmounts);
        return BigDecimal.ZERO.setScale(2);
    }
}
