package com.example.sennova.domain.model.testRequest;

import java.time.LocalDate;
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

     private LocalDate sampling_time;

    private LocalDate sampling_date;

    private String sampleCode;

    private double gross_weight;

    private double temperature;

    private String package_description;

    private String storage_conditions;

    private String observations;

    private Boolean statusReception;

    private String sampleImage;

    private Integer totalAnalysis;
    
   private List<SampleAnalysisModel> analysisEntities;

    @JsonIgnore
    private TestRequestModel testRequest;

}
