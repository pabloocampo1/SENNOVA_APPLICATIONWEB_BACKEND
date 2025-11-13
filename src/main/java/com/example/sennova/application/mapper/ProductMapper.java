package com.example.sennova.application.mapper;

import com.example.sennova.application.dto.productDtos.ProductRequestDto;
import com.example.sennova.application.dto.productDtos.ProductResponseBasicDto;
import com.example.sennova.domain.model.ProductModel;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductModel toModel(ProductRequestDto productRequestDto);
    ProductResponseBasicDto toResponse(ProductModel productModel);

    List<ProductResponseBasicDto> toResponse(Iterable<ProductModel> productModelIterable);
}
