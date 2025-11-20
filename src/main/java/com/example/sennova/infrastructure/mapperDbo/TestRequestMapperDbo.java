package com.example.sennova.infrastructure.mapperDbo;

import com.example.sennova.domain.model.testRequest.TestRequestModel;
import com.example.sennova.infrastructure.persistence.entities.analysisRequestsEntities.TestRequestEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        uses = {SampleMapperDbo.class, CustomerMapperDbo.class, UserMapperDbo.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface TestRequestMapperDbo {

    @Mapping(source = "sampleEntityList", target = "samples")
    TestRequestModel toModel(TestRequestEntity entity);

    @Mapping(source = "samples", target = "sampleEntityList")
    @Mapping(source = "members", target = "members")
    TestRequestEntity toEntity(TestRequestModel model);
}

