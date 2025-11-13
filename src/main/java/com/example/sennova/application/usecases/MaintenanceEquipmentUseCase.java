package com.example.sennova.application.usecases;

import com.example.sennova.application.dto.inventory.EquipmentInventory.request.MaintenanceEquipmentRequest;
import com.example.sennova.domain.model.MaintenanceRecordEquipmentModel;

import java.util.List;

public interface MaintenanceEquipmentUseCase {
    MaintenanceRecordEquipmentModel save(MaintenanceEquipmentRequest maintenanceEquipmentRequest);
    List<MaintenanceRecordEquipmentModel> getAllByEquipmentId(Long equipmentId);
    Boolean deleteById(Long id);
}
