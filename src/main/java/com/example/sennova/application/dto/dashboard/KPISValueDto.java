package com.example.sennova.application.dto.dashboard;

// TRENDS
// UP / DOWN / SAME

import lombok.Data;

@Data
public class KPISValueDto {
    private Integer currentValue;
    private Integer previousValue;
    private Double percentageChange;
    private String trend;
}
