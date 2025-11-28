package com.example.sennova.domain.model.testRequest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Getter
@Setter
public class SampleModel {

    private Long sampleId;

    private String matrix;

    private String description;

     private LocalDate sampleEntryDate;

    private LocalDate sampleReceptionDate;

    private String sampleCode;

    private double gross_weight;

    private double temperature;

    private String packageDescription;
    private String identificationSample;

    private LocalDate createAt;

    private String storageConditions;

    private String observations;

    // This due date is generated when the laboratory registers the reception.
// It's slightly different from the test request due date, which is generated
// when all samples have been received.
    private LocalDate dueDate;

    private Boolean statusReception;

    private String sampleImage;

    private Integer totalAnalysis;
    
   private List<SampleAnalysisModel> analysisEntities;

    @JsonIgnore
    private TestRequestModel testRequest;

}
