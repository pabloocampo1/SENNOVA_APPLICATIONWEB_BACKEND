package com.example.sennova.application.dto.productDtos;

import com.example.sennova.application.dto.UserDtos.UserListResponse;
import com.example.sennova.domain.model.AnalysisModel;
import com.example.sennova.domain.model.MatrixModel;
import com.example.sennova.domain.model.UserModel;

import java.time.LocalDate;
import java.util.List;

public record AnalysisInfoDtoResponse(
        Long analysisId,
        String analysisName,
        String method,
        String equipment,
        String units,
        double price,
        String notes,
        LocalDate createAt,
        LocalDate updateAt,
        List<MatrixModel> matrices,
        List<UserListResponse> qualifiedUsers
) {
    // Constructor estático para convertir desde el modelo de dominio
    public static AnalysisInfoDtoResponse fromModel(AnalysisModel model, List<UserListResponse> usersDto) {
        return new AnalysisInfoDtoResponse(
                model.getAnalysisId(),
                model.getAnalysisName(),
                model.getMethod(),
                model.getEquipment(),
                model.getUnits(),
                model.getPrice(),
                model.getNotes(),
                model.getCreateAt(),
                model.getUpdateAt(),
                model.getMatrices(),
                usersDto
        );
    }
}