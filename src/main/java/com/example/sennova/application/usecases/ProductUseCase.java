package com.example.sennova.application.usecases;

import com.example.sennova.application.dto.productDtos.AnalysisAssignMatrixRequest;
import com.example.sennova.application.dto.productDtos.AnalysisAssignedQualifiedUsers;
import com.example.sennova.application.dto.productDtos.AnalysisInfoDtoResponse;
import com.example.sennova.domain.model.AnalysisModel;
import com.example.sennova.domain.model.MatrixModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductUseCase {
    Page<AnalysisModel> getAll(Pageable pageable);
    List<AnalysisModel> all();
    AnalysisModel getById(Long id);
    List<AnalysisModel> getByName(String name);
    void deleteProduct(Long id);
    AnalysisModel editProduct(AnalysisModel analysisModel, Long id);
    AnalysisModel save(AnalysisModel analysisModel);
    AnalysisInfoDtoResponse getAllInfoById(Long id);
    AnalysisInfoDtoResponse assignQualifiedUsers(AnalysisAssignedQualifiedUsers dto);
    AnalysisInfoDtoResponse removeUser(Long userId, Long analysisId);
    AnalysisInfoDtoResponse    assignMatrices(AnalysisAssignMatrixRequest dto);
    AnalysisInfoDtoResponse removeMatrix(Long analysisId, Long matrixId);
    List<AnalysisModel> getAnalysisByMatrix(Long matrix);
    // matrix

    MatrixModel saveMatrix(MatrixModel m);
    List<MatrixModel> getAllMatrix();
    List<MatrixModel> getAllMatrixAvailable();
}
