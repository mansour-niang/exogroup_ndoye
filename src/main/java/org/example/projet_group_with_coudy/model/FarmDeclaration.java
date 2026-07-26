package org.example.projet_group_with_coudy.model;

import java.math.BigDecimal;

/**
 * Bilan de fin de saison d'une exploitation agricole. Record immuable :
 * garantit qu'une declaration ne peut pas etre modifiee une fois recue par
 * le moteur de calcul.
 */
public record FarmDeclaration(
        String farmId,
        BigDecimal hectares,
        CropType cropType,
        boolean organicCertified,
        BigDecimal declaredYieldPerHectare,
        String location
) {
}
