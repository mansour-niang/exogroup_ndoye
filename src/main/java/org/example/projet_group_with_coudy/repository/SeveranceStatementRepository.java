package org.example.projet_group_with_coudy.repository;

import org.example.projet_group_with_coudy.model.SeveranceStatement;

/**
 * Persistance de l'historique des soldes de tout compte calcules.
 */
public interface SeveranceStatementRepository {
    void save(SeveranceStatement statement);
}
