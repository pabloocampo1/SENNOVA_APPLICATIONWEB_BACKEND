package com.example.sennova.infrastructure.mapperDbo;

import com.example.sennova.domain.model.testRequest.SampleModel;
import com.example.sennova.infrastructure.persistence.entities.analysisRequestsEntities.SampleEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = { SampleAnalysisMapperDbo.class })
public interface SampleMapperDbo {

    SampleModel toModel(SampleEntity sampleEntity);
    SampleEntity toEntity(SampleModel sampleModel);
}
