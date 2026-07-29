package org.example.projet_group_with_coudy.repository;

import org.example.projet_group_with_coudy.model.FinancingPlan;

/**
 * Persistance de l'historique des plans de financement calcules.
 */
public interface FinancingPlanRepository {
    void save(FinancingPlan plan);
}
