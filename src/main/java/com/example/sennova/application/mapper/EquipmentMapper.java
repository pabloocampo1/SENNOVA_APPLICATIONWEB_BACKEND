package com.example.sennova.application.mapper;

import com.example.sennova.application.dto.inventory.EquipmentInventory.request.EquipmentRequestDto;
import com.example.sennova.application.dto.inventory.EquipmentInventory.response.EquipmentResponseDto;
import com.example.sennova.domain.model.EquipmentModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EquipmentMapper {

    @Mapping(target = "responsibleId", source = "responsible.userId")
    @Mapping(target = "responsibleName", source = "responsible.name")
    @Mapping(target = "locationId", source = "location.equipmentLocationId")
    @Mapping(target = "locationName", source = "location.locationName")
    @Mapping(target = "usageId", source = "usage.equipmentUsageId")
    @Mapping(target = "usageName", source = "usage.usageName")
    EquipmentResponseDto toResponse(EquipmentModel equipmentModel);

    EquipmentModel toDomain(EquipmentRequestDto equipmentRequestDto);
}

