package com.example.sennova.application.dto.UserDtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class UserCompetenceDTO {
    private Long userId;
    private String name;
    private String position;
    private String imageProfile;
    private List<AnalysisDTO> qualifiedAnalyses;

    @Data
    @AllArgsConstructor
    public static class AnalysisDTO {
        private Long analysisId;
        private String analysisName;
    }
}

