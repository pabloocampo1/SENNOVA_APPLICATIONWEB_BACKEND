package com.example.sennova.domain.model;

import com.example.sennova.infrastructure.persistence.entities.Analisys.AnalysisEntity;

import java.util.List;

public class MatrixModel {

    private Long matrixId;

    private String matrixName;

    private Boolean available;

    private List<AnalysisModel> analysis;


    public MatrixModel(Long matrixId, String matrixName, Boolean available) {
        this.matrixId = matrixId;
        this.matrixName = matrixName;
        this.available = available;
    }

    public MatrixModel(String matrixName, Boolean available) {
        this.matrixName = matrixName;
        this.available = available;
    }

    public MatrixModel(Long matrixId, String matrixName, Boolean available, List<AnalysisModel> analysis) {
        this.matrixId = matrixId;
        this.matrixName = matrixName;
        this.available = available;
        this.analysis = analysis;
    }

    public MatrixModel() {
    }

    public List<AnalysisModel> getAnalysis() {
        return analysis;
    }

    public void setAnalysis(List<AnalysisModel> analysis) {
        this.analysis = analysis;
    }

    public Long getMatrixId() {
        return matrixId;
    }

    public void setMatrixId(Long matrixId) {
        this.matrixId = matrixId;
    }

    public String getMatrixName() {
        return matrixName;
    }

    public void setMatrixName(String matrixName) {
        this.matrixName = matrixName;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }
}
