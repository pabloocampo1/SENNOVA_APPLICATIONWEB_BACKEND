package com.example.sennova.infrastructure.mapperDbo;

import com.example.sennova.domain.model.testRequest.CustomerModel;
import com.example.sennova.infrastructure.persistence.entities.CustomerEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerMapperDbo {
    CustomerModel toModel(CustomerEntity customer);
    CustomerEntity  toEntity(CustomerModel customerModel);
}
