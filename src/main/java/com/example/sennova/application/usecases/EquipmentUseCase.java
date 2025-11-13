package com.example.sennova.application.usecases;

import com.example.sennova.domain.model.EquipmentModel;
import com.example.sennova.infrastructure.persistence.entities.inventoryEquipmentEntities.EquipmentEntity;
import com.example.sennova.infrastructure.persistence.entities.inventoryEquipmentEntities.EquipmentMediaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface EquipmentUseCase {
    EquipmentModel save(EquipmentModel equipmentModel,Long responsibleId, Long locationId, Long usageId);
    EquipmentModel update(EquipmentModel equipmentModel, Long id,Long responsibleId, Long locationId, Long usageId);
    EquipmentModel getById(Long id);
    List<EquipmentModel> getAllByName(String name);
    List<EquipmentModel> getAllByInternalCode(String internalCode);
    List<EquipmentModel> getAllBySenaInventoryTag(String code);
    Page<EquipmentModel> getAll(Pageable pageable);
    List<EquipmentModel> getAll();
    void deleteById(Long id);
    List<EquipmentModel> getByLocation(Long locationId);
    List<EquipmentModel> getByUsage(Long usageId);
    EquipmentModel changeState(Long id, String state);
    String changeImage(MultipartFile multipartFile, Long equipmentId);
    List<EquipmentMediaEntity> saveFiles(List<MultipartFile> files, Long equipmentId);
    EquipmentEntity returnEntityById(Long id);
    List<EquipmentMediaEntity> getFiles(Long id);
    Boolean deleteFile(String public_Id);
    Map<String, Long> getSummaryStatics();
    List<EquipmentModel> getAllEquipmentToMaintenance();
    EquipmentModel reportEquipment(Long equipmentId);
    EquipmentModel markEquipmentAsExisting(Long equipmentId);
    List<EquipmentModel> getAllReportedEquipment();

}
