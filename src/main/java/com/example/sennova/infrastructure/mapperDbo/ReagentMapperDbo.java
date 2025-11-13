package com.example.sennova.infrastructure.mapperDbo;

import com.example.sennova.domain.model.ReagentModel;
import com.example.sennova.infrastructure.persistence.entities.inventoryReagentsEntities.ReagentsEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {LocationMapperDbo.class, UsageMapperDbo.class, UserMapperDbo.class})
public interface ReagentMapperDbo {
    ReagentModel toModel(ReagentsEntity reagentsEntity);
    ReagentsEntity toEntity(ReagentModel reagentModel);
}
