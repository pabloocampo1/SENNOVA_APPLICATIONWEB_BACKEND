package com.example.sennova.application.mapper;

import com.example.sennova.application.dto.testeRequest.CustomerRequestRecord;
import com.example.sennova.application.dto.testeRequest.CustomerResponse;
import com.example.sennova.domain.model.testRequest.CustomerModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    CustomerModel toModel(CustomerRequestRecord customerRequestRecord);
    CustomerResponse toResponse(CustomerModel customerModel);

}
