package com.example.sennova.infrastructure.mapperDbo;

import com.example.sennova.domain.model.MaintenanceRecordEquipmentModel;
import com.example.sennova.infrastructure.persistence.entities.inventoryEquipmentEntities.MaintenanceRecordsEquipment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {EquipmentMapperDbo.class})
public interface MaintenanceMapperDbo {

    MaintenanceRecordEquipmentModel toModel(MaintenanceRecordsEquipment maintenanceRecordsEquipment);
    MaintenanceRecordsEquipment toEntity(MaintenanceRecordEquipmentModel maintenanceRecordEquipmentModel);
}
