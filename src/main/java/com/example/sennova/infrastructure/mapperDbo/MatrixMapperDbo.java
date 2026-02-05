package com.example.sennova.infrastructure.mapperDbo;

import com.example.sennova.domain.model.MatrixModel;
import com.example.sennova.infrastructure.persistence.entities.Analisys.MatrixEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
public interface MatrixMapperDbo {
    @Mapping(target = "analysis", ignore = true)
    MatrixModel matrixToModel(MatrixEntity matrix);

    @Mapping(target = "analysis", ignore = true)
    MatrixEntity matrixToEntity(MatrixModel matrixModel);
}
