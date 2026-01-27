package com.example.sennova.application.dto.dashboard;

import lombok.Data;

import java.util.List;

@Data
public class MetricsStateSampleTestRequest {
    private SampleStatusMetricsDTO sampleStatusMetrics;
    private TestRequestStateMetricsDTO testRequestStateMetrics;

    public MetricsStateSampleTestRequest(SampleStatusMetricsDTO sampleStatusMetrics, TestRequestStateMetricsDTO testRequestStateMetrics) {
        this.sampleStatusMetrics = sampleStatusMetrics;
        this.testRequestStateMetrics = testRequestStateMetrics;
    }
}
