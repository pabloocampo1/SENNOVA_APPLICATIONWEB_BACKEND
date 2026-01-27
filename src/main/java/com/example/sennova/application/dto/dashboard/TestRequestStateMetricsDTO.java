package com.example.sennova.application.dto.dashboard;

import lombok.Data;

@Data
public class TestRequestStateMetricsDTO {
    private Long pending;
    private Long accepted;
    private Long rejected;
}
