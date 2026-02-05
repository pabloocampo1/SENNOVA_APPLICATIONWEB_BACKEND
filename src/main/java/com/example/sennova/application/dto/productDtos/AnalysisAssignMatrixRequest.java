package com.example.sennova.application.dto.productDtos;

import java.util.List;

public record AnalysisAssignMatrixRequest(
        Long analysisId,
        List<Long> matrices
) {
}
