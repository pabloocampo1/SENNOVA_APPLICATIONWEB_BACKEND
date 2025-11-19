package com.example.sennova.domain.model.testRequest;

import java.time.LocalDate;

import com.example.sennova.domain.model.ProductModel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class SampleAnalysisModel {
    private Long SampleProductAnalysisId;


    private String resultFinal;

    private LocalDate resultDate;

    private String notes;

    private String code;

    private ProductModel product;

    private SampleModel sample;

    private Boolean stateResult;
}
