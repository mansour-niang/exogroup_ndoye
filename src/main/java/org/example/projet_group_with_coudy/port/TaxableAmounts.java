package org.example.projet_group_with_coudy.port;

import java.math.BigDecimal;

/**
 * Ventilation des montants transmise a l'administration fiscale (Besoin 4) :
 * le montant brut total et la part de ce brut correspondant a la prime
 * d'anciennete, qui beneficie d'une exoneration specifique que seul le
 * systeme fiscal externe sait appliquer.
 */
public record TaxableAmounts(BigDecimal grossAmount, BigDecimal seniorityBonusAmount) {
}
