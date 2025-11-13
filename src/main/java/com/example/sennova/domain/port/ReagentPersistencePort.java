package com.example.sennova.domain.port;

import com.example.sennova.application.dto.inventory.ReagentInventory.ReagentSummaryStatistics;
import com.example.sennova.domain.model.LocationModel;
import com.example.sennova.domain.model.ReagentModel;
import com.example.sennova.domain.model.UsageModel;
import com.example.sennova.infrastructure.persistence.entities.inventoryReagentsEntities.ReagentsEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface ReagentPersistencePort {
    ReagentModel save(ReagentModel reagentModel);
    ReagentModel saveDirect(ReagentModel reagentModel);
    ReagentModel update(ReagentModel reagentModel);

    List<ReagentModel> findAll();
    Page<ReagentModel> findAll(Pageable pageable);
    List<ReagentModel> findAllByName(String name);
    List<ReagentModel> findAllByExpirationDate(LocalDate currentDate);
    List<ReagentModel> findAllByLocation(LocationModel locationModel);
    List<ReagentModel> findAllByInventoryTag(String inventoryTag);
    List<ReagentModel> findAllByUsage(UsageModel usageModel);
    ReagentModel findById(Long id);
    ReagentsEntity findEntityById(Long id);
    void deleteById(Long id);
    ReagentSummaryStatistics getSummaryStatics();

}
