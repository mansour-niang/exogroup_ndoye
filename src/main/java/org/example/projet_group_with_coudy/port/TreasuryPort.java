package org.example.projet_group_with_coudy.port;

import java.math.BigDecimal;

/**
 * Contrat vers l'API du Tresor Public (Besoin 5). Notifie pour demander une
 * derogation exceptionnelle lorsque la bourse mensuelle calculee depasse le
 * plafond legal ; le dossier passe alors en validation manuelle.
 */
public interface TreasuryPort {
    void requestExceptionalWaiver(String studentId, BigDecimal monthlyScholarship);
}
