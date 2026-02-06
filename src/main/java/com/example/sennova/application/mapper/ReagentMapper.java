package com.example.sennova.application.mapper;

import com.example.sennova.application.dto.inventory.ReagentInventory.ReagentRequestDto;
import com.example.sennova.application.dto.inventory.ReagentInventory.ReagentResponseDto;
import com.example.sennova.domain.model.ReagentModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReagentMapper {

   
    @Mapping(target = "locationId", source = "location.equipmentLocationId")
    @Mapping(target = "locationName", source = "location.locationName")
    @Mapping(target = "usageId", source = "usage.equipmentUsageId")
    @Mapping(target = "usageName", source = "usage.usageName")
    ReagentResponseDto toResponse(ReagentModel reagentModel);
    ReagentModel toModel(ReagentRequestDto reagentRequestDto);
}
