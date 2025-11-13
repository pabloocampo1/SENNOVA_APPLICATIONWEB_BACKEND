package com.example.sennova.application.mapper;

import com.example.sennova.application.dto.inventory.LocationResponseDto;
import com.example.sennova.application.dto.inventory.LocationRequestDto;
import com.example.sennova.domain.model.LocationModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LocationMapper {

    LocationResponseDto toResponse(LocationModel locationModel);
    LocationModel toModel(LocationRequestDto locationRequestDto);
}
