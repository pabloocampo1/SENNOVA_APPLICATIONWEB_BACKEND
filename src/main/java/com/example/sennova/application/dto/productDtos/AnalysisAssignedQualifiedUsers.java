package com.example.sennova.application.dto.productDtos;

import java.util.List;

public record AnalysisAssignedQualifiedUsers(
        Long analysisId,
        List<Long> users
) {
}
