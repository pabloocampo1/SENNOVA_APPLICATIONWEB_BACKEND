package com.example.sennova.application.dto.testeRequest;

import com.example.sennova.domain.model.testRequest.SampleResults;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class SampleInfoExecutionDto {

    // customer info
    private String customerName;
    private String customerEmail;


    // sample info
    private Long sampleId;
    private String matrix;
    private  Integer totalAnalysis;
    private Integer totalAnalysisFinished;
    private List<SampleResults> results;


    private String testRequestCode;
    private LocalDate testRequestDueDate;
}
