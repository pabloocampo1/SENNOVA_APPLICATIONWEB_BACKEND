package com.example.sennova.infrastructure.mapperDbo;

import com.example.sennova.domain.model.AnalysisModel;
import com.example.sennova.infrastructure.persistence.entities.Analisys.AnalysisEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {UserMapperDbo.class, MatrixMapperDbo.class})
public interface ProductMapperDbo {

    AnalysisEntity toEntity(AnalysisModel analysisModel);
    AnalysisModel toModel(AnalysisEntity analysisEntity);
}
