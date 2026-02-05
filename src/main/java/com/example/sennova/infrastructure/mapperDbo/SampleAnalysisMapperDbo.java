package com.example.sennova.infrastructure.mapperDbo;

import com.example.sennova.domain.model.testRequest.SampleAnalysisModel;
import com.example.sennova.infrastructure.persistence.entities.requestsEntities.SampleAnalysisEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = { ProductMapperDbo.class})
public interface SampleAnalysisMapperDbo {
    @Mapping(target = "sample", ignore = true)
    SampleAnalysisModel toModel(SampleAnalysisEntity entity);

   // @Mapping(target = "sample", ignore = true)
    SampleAnalysisEntity toEntity(SampleAnalysisModel model);
}
