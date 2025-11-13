package com.example.sennova.application.usecases;

import com.example.sennova.application.dto.inventory.ReagentInventory.ReagentSummaryStatistics;
import com.example.sennova.application.dto.inventory.ReagentInventory.UsageReagentRequest;
import com.example.sennova.domain.model.ReagentModel;
import com.example.sennova.infrastructure.persistence.entities.inventoryReagentsEntities.ReagentMediaFilesEntity;
import com.example.sennova.infrastructure.persistence.entities.inventoryReagentsEntities.ReagentsEntity;
import com.example.sennova.infrastructure.persistence.entities.inventoryReagentsEntities.ReagentsUsageRecords;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ReagentUseCase {
    ReagentModel save(ReagentModel reagentModel, MultipartFile multipartFile, String performedBy, Long responsibleId, Long locationId, Long usageId);
    ReagentModel saveDirect(ReagentModel reagentModel);
    ReagentModel update(ReagentModel reagentModel, Long reagentId, MultipartFile multipartFile,  Long responsibleId, Long locationId, Long usageId);
    ReagentModel getById(Long id);
    ReagentsEntity getEntity(Long id);
    List<ReagentModel> getAll();
    Page<ReagentModel> getAll(Pageable pageable);
    List<ReagentModel> getAllByName(String name);
    List<ReagentModel> getAllExpired();
    List<ReagentModel> getAllByLocation(Long locationId);
    void deleteById(Long id);
    boolean deleteFile(String id);
    List<ReagentMediaFilesEntity> getFiles(Long reagentId);
    List<ReagentMediaFilesEntity> uploadFiles(Long reagentId, List<MultipartFile> multipartFiles);
    ReagentModel changeQuantity(Long reagentId, Integer quantity);
    ReagentModel changeState(Long reagentId, String state);
    ReagentsUsageRecords saveUsage(UsageReagentRequest usageReagentRequest);
    List<ReagentsUsageRecords> getUsagesByReagentId(Long reagentId);
    ReagentSummaryStatistics getSummaryStatics();



}
