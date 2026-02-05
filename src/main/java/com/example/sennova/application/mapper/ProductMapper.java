package com.example.sennova.application.mapper;

import com.example.sennova.application.dto.productDtos.AnalysisInfoDtoResponse;
import com.example.sennova.application.dto.productDtos.ProductRequestDto;
import com.example.sennova.application.dto.productDtos.ProductResponseBasicDto;
import com.example.sennova.domain.model.AnalysisModel;
import com.example.sennova.domain.model.MatrixModel;
import com.example.sennova.infrastructure.mapperDbo.MatrixMapperDbo;
import com.example.sennova.infrastructure.mapperDbo.UserMapperDbo;
import com.example.sennova.infrastructure.persistence.entities.Analisys.MatrixEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring",  uses = {UserMapperDbo .class, MatrixMapperDbo .class})
public interface ProductMapper {

    AnalysisModel toModel(ProductRequestDto productRequestDto);
    ProductResponseBasicDto toResponse(AnalysisModel analysisModel);



    List<ProductResponseBasicDto> toResponse(Iterable<AnalysisModel> productModelIterable);
}
