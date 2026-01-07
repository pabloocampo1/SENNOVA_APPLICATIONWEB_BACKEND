package com.example.sennova.application.dto.testeRequest.sample;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class SampleDeliveredResponse {

    private Long sampleId;
    private LocalDateTime deliveryDate;
    private String sampleCode;
    private String matrix;

    private String customerName;
    private String customerEmail;
    private String testRequestCode;
    private Long testRequestId;
}
