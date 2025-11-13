package com.example.sennova.infrastructure.mapperDbo;

import com.example.sennova.domain.model.LocationModel;
import com.example.sennova.infrastructure.persistence.entities.LocationEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LocationMapperDbo {

    LocationModel toModel(LocationEntity locationEntity);
    LocationEntity toEntity(LocationModel locationModel);
}
