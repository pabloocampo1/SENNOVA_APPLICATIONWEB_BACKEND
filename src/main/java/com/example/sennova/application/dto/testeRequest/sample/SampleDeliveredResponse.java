package com.example.sennova.application.dto.testeRequest.sample;

import lombok.Data;

import java.time.LocalDate;

@Data
public class SampleDeliveredResponse {

    private Long sampleId;
    private LocalDate deliveryDate;
    private String sampleCode;
    private String matrix;

    private String customerName;
    private String customerEmail;
    private String testRequestCode;
    private Long testRequestId;
}
