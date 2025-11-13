package com.example.sennova.infrastructure.mapperDbo;

import com.example.sennova.domain.model.EquipmentLoanModel;
import com.example.sennova.infrastructure.persistence.entities.inventoryEquipmentEntities.EquipmentLoanEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {EquipmentMapperDbo.class})
public interface EquipmentLoanMapperDbo {

    EquipmentLoanEntity toEntity(EquipmentLoanModel equipmentLoanModel);


    EquipmentLoanModel toModel(EquipmentLoanEntity equipmentLoanEntity);
}
