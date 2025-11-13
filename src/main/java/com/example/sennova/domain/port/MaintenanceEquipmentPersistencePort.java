package com.example.sennova.domain.port;

import com.example.sennova.domain.model.MaintenanceRecordEquipmentModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MaintenanceEquipmentPersistencePort {
    MaintenanceRecordEquipmentModel save(MaintenanceRecordEquipmentModel maintenanceRecordEquipmentModel);
    List<MaintenanceRecordEquipmentModel> findAllByEquipmentId(Long equipmentId);
    Boolean existById(Long id);
    Boolean deleteById(Long id);
    void deleteByEquipmentId(Long equipmentId);
}
