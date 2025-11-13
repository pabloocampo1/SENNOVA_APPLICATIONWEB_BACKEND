package com.example.sennova.application.mapper;

import com.example.sennova.application.dto.testeRequest.TestRequestResponse;
import com.example.sennova.domain.model.testRequest.TestRequestModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TestRequestMapper {

    TestRequestResponse toResponse (TestRequestModel testRequestModel);
}
