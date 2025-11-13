package com.example.sennova.application.mapper;

import com.example.sennova.application.dto.inventory.UsageRequestDto;
import com.example.sennova.application.dto.inventory.UsageResponseDto;
import com.example.sennova.domain.model.UsageModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UsageMapper {
    UsageResponseDto toResponse(UsageModel usageModel);
    UsageModel toModel(UsageRequestDto usageRequestDto);
}
