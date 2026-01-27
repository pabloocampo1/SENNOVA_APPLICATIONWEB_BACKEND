package com.example.sennova.application.dto.dashboard;

import lombok.Data;

import java.util.List;

@Data
public class ProductAnalysisCountDto {
    private  List<ProductsMoreUsedDto> productsMoreUsed;
    private List<MatrixMoreUsedDto> matrixMoreUsed;

}
