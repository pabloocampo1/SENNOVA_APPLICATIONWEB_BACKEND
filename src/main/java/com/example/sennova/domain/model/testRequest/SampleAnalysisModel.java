package com.example.sennova.domain.model.testRequest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.example.sennova.domain.model.AnalysisModel;
import com.example.sennova.infrastructure.persistence.entities.requestsEntities.SampleProductDocumentResult;
import lombok.Data;

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

    private AnalysisModel product;

    private SampleModel sample;

    private String incert;

    private String valueRef;

    private LocalDateTime updateAt;

    private LocalDateTime createAt;

    private Boolean stateResult;

    private List<SampleProductDocumentResult> sampleProductDocumentResult;
}
