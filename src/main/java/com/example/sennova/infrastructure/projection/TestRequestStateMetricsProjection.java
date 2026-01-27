package com.example.sennova.infrastructure.projection;

public interface TestRequestStateMetricsProjection {

    Long getPending();

    Long getAccepted();

    Long getRejected();
}

