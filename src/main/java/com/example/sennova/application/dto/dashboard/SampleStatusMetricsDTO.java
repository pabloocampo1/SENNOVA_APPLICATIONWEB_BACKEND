package com.example.sennova.application.dto.dashboard;

import lombok.Data;

@Data
public class SampleStatusMetricsDTO {
    private Long withoutReception;
    private Long inProcess;
    private Long overDue;
    private Long delivered;
}
