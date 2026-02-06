package com.example.sennova.infrastructure.projection;

import java.util.List;

public interface UserWithAnalysisProjection {
    Long getUserId();
    String getName();
    String getPosition();
    String getImageProfile();

    // Spring mapeará automáticamente los análisis asociados
    List<AnalysisShortProjection> getTrainedAnalyses();

    interface AnalysisShortProjection {
        Long getAnalysisId();
        String getAnalysisName();
    }
}
