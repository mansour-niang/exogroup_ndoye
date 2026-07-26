package org.example.projet_group_with_coudy.port;

import java.math.BigDecimal;

/**
 * Contrat vers le systeme de l'Inspection Phytosanitaire, couple au registre
 * des douanes et des importations agrochimiques (Besoin 5). Notifie pour
 * declencher un controle sur site lorsque le montant final alloue depasse
 * strictement le seuil de vigilance.
 */
public interface PhytosanitaryInspectionPort {
    void reportForInspection(String farmId, BigDecimal finalAmount);
}
