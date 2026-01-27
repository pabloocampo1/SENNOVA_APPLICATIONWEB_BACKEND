package com.example.sennova.infrastructure.projection;

public interface SampleStatusMetricsProjection {

    Long getWithoutReception();

    Long getProcess();

    Long getOverDue();

    Long getDelivered();
}
