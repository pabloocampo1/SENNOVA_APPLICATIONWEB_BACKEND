package com.example.sennova.application.dto.testeRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SamplesByTestRequestDto {
    private String sample;                
    private String analysis;
    private Double priceByAnalysis;
    private Long quantityAnalysisBySample;
    private Double total;
}
