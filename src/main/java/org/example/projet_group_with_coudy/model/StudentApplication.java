package org.example.projet_group_with_coudy.model;

import java.math.BigDecimal;

/**
 * Dossier d'un etudiant soumis pour la rentree academique. Record immuable :
 * garantit qu'un dossier ne peut pas etre modifie une fois recu par le
 * moteur de calcul. {@code previousYearAverage} est {@code null} pour un
 * etudiant entrant en premiere annee.
 */
public record StudentApplication(
        String studentId,
        StudyCycle studyCycle,
        BigDecimal familyAnnualIncome,
        BigDecimal homeDistanceKm,
        BaccalaureateMention baccalaureateMention,
        BigDecimal previousYearAverage,
        boolean repeatingYear,
        boolean repeatMedicallyJustified
) {
}
