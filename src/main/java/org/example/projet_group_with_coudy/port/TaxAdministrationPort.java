package org.example.projet_group_with_coudy.port;

import java.math.BigDecimal;

/**
 * Contrat vers le systeme externe de l'administration fiscale (Besoin 4).
 * Le moteur ne calcule jamais l'impot lui-meme : il transmet la ventilation
 * des montants et recoit la retenue fiscale exacte a appliquer.
 */
public interface TaxAdministrationPort {
    BigDecimal calculateWithholding(TaxableAmounts taxableAmounts);
}
