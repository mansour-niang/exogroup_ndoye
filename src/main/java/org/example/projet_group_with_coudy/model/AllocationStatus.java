package org.example.projet_group_with_coudy.model;

/**
 * Statut de l'allocation de subvention. Passe a EN_ATTENTE_AUDIT si le
 * montant final depasse strictement 10 000 000 XOF (Besoin 5).
 */
public enum AllocationStatus {
    ALLOUE,
    EN_ATTENTE_AUDIT
}
