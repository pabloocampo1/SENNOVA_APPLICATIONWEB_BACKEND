package com.example.sennova.infrastructure.projection;

public interface SampleInfoSummaryTestRequestProjection {

    String getSample();
    String getAnalysis();
    Double getPriceByAnalysis();
    Long getQuantityAnalysisBySample();
    Double getTotal();     ;
}
