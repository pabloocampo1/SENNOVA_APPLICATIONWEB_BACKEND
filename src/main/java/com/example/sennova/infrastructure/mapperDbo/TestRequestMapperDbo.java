package com.example.sennova.infrastructure.mapperDbo;

import com.example.sennova.domain.model.testRequest.TestRequestModel;
import com.example.sennova.infrastructure.persistence.entities.analysisRequestsEntities.TestRequestEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {SampleMapperDbo.class, CustomerMapperDbo.class})
public interface TestRequestMapperDbo {
    @Mapping(source = "sampleEntityList", target = "samples")
    TestRequestModel toModel(TestRequestEntity testRequestEntity);

    @Mapping(source = "samples", target = "sampleEntityList")
    TestRequestEntity toEntity(TestRequestModel testRequestModel);
}
