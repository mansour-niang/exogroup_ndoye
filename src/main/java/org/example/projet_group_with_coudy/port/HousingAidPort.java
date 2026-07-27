package org.example.projet_group_with_coudy.port;

import java.math.BigDecimal;

/**
 * Contrat vers le registre national des aides sociales (Besoin 4). Le
 * moteur ne connait pas les aides deja percues par l'etudiant : il
 * interroge ce port pour obtenir le montant exact d'une eventuelle aide au
 * logement, a deduire de la bourse universitaire (non-cumul).
 */
public interface HousingAidPort {
    BigDecimal getHousingAidAmount(String studentId);
}
