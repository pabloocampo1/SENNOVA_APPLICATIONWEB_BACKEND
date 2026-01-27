package com.example.sennova.infrastructure.projection;

public interface ReagentsMetricsProjection {
    Long getActives();
    Long getExpired();
    Long getWithoutStock();
}
