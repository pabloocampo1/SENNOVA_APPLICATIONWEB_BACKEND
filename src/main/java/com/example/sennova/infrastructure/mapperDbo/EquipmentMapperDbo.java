package com.example.sennova.infrastructure.mapperDbo;

import com.example.sennova.domain.model.EquipmentModel;
import com.example.sennova.infrastructure.persistence.entities.inventoryEquipmentEntities.EquipmentEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {UsageMapperDbo.class, LocationMapperDbo.class, UserMapperDbo.class})
public interface EquipmentMapperDbo {

    EquipmentModel toModel(EquipmentEntity equipmentEntity);
    EquipmentEntity toEntity(EquipmentModel equipmentModel);
}
