package com.example.sennova.infrastructure.mapperDbo;

import com.example.sennova.domain.model.ProductModel;
import com.example.sennova.infrastructure.persistence.entities.analysisRequestsEntities.ProductEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapperDbo {

    ProductEntity toEntity(ProductModel productModel);
    ProductModel toModel(ProductEntity productEntity);
}
