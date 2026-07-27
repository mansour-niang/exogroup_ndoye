package org.example.projet_group_with_coudy.repository;

import org.example.projet_group_with_coudy.model.SubsidyAllocation;

/**
 * Persistance de l'historique des allocations de subvention calculees.
 */
public interface SubsidyAllocationRepository {
    void save(SubsidyAllocation allocation);
}
