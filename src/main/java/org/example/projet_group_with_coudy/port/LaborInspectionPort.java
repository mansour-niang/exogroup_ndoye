package org.example.projet_group_with_coudy.port;

import java.math.BigDecimal;

/**
 * Contrat vers le systeme informatique de l'Inspection du Travail (Besoin 5).
 * Notifie pour declencher un audit lorsque le montant net d'un solde de tout
 * compte depasse strictement le seuil de transparence.
 */
public interface LaborInspectionPort {
    void reportSeveranceForAudit(String employeeId, BigDecimal netAmount);
}
