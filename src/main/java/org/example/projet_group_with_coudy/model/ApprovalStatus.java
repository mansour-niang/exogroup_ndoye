package org.example.projet_group_with_coudy.model;

/**
 * Statut d'approbation du plan de financement. Passe a
 * VALIDATION_MANUELLE_REQUISE si la bourse mensuelle depasse le plafond
 * legal (Besoin 5).
 */
public enum ApprovalStatus {
    APPROUVE,
    VALIDATION_MANUELLE_REQUISE
}
