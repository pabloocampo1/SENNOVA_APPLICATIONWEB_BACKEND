package com.example.sennova.application.dto.testeRequest.sample;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class SampleWithoutReceptionResponse {
    private Long sampleId;
    private String sampleCode;
    private String matrix;
    private Integer totalAnalysis;
    private String statusReception;
    private String customerName;
    private String testRequestCode;
    private Long testRequestId;
}
