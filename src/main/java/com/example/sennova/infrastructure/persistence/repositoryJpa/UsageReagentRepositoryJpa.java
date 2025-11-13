package com.example.sennova.infrastructure.persistence.repositoryJpa;

import com.example.sennova.infrastructure.persistence.entities.inventoryReagentsEntities.ReagentsUsageRecords;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UsageReagentRepositoryJpa extends JpaRepository<ReagentsUsageRecords, Long> {
    List<ReagentsUsageRecords> findAllByReagent_ReagentsId(Long reagentId);
}
