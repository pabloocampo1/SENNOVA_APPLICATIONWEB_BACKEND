package com.example.sennova.application.dto.UserDtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AnalysisSummaryDTO {
    private Long analysisId;
    private String analysisName;
}