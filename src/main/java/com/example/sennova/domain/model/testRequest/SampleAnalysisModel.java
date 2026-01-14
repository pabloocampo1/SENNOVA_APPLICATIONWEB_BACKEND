package com.example.sennova.domain.model.testRequest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.example.sennova.domain.model.ProductModel;
import com.example.sennova.infrastructure.persistence.entities.analysisRequestsEntities.SampleProductDocumentResult;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class SampleAnalysisModel {
    private Long SampleProductAnalysisId;

    private String resultFinal;

    private LocalDate resultDate;

    private String unit;

    private String passStatus;

    private String accreditationStatus;

    private String resultGeneratedBy;

    private String standards;

    private String notes;

    private String code;

    private ProductModel product;

    private SampleModel sample;

    private LocalDateTime updateAt;

    private LocalDateTime createAt;

    private Boolean stateResult;

    private List<SampleProductDocumentResult> sampleProductDocumentResult;
}
