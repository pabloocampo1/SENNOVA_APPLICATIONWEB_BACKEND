package com.example.sennova.domain.port;

import com.example.sennova.domain.model.LocationModel;
import com.example.sennova.domain.model.EquipmentModel;
import com.example.sennova.domain.model.UsageModel;
import com.example.sennova.infrastructure.persistence.entities.inventoryEquipmentEntities.EquipmentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface EquipmentPersistencePort {

    EquipmentModel save(EquipmentModel equipmentModel);
    EquipmentModel update( EquipmentModel equipmentModel);
    Page<EquipmentModel> getAllPage(Pageable pageable);
    EquipmentModel findById(Long id);
    List<EquipmentModel> findAllByInternalCode(String internalCode);
    List<EquipmentModel> findAllBySenaInventoryTag(String code);
    List<EquipmentModel> findAll();
    List<EquipmentModel> findAllByName(String name);
    Boolean existById(Long id);
    EquipmentModel changeState(Long id, String state);
    List<EquipmentModel> findAllByLocation(LocationModel locationModel);
    List<EquipmentModel> findAllByUsage(UsageModel usageModel);
    void delete(Long id);
    Boolean existsBySerialNumber(String serialNumber);
    Boolean existsByInternalCode(String internalCode);
    EquipmentEntity findEntityById(Long id);
    long countByAvailableTrue();
    long countReported();
    long countByMaintenanceMonth();
    long countTotal();
    List<EquipmentModel> findAllByMaintenanceDate(LocalDate currentDate);
    List<EquipmentModel> findAllByIsPresentFalse();


}
