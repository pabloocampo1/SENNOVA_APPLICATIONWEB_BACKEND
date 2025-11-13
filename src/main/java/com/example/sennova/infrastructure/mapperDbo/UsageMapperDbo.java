package com.example.sennova.infrastructure.mapperDbo;

import com.example.sennova.domain.model.UsageModel;
import com.example.sennova.infrastructure.persistence.entities.UsageEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UsageMapperDbo {

    UsageEntity toEntity(UsageModel usageModel);
    UsageModel toModel(UsageEntity usageEntity);
}
