package com.example.sennova.domain.port;

import com.example.sennova.domain.model.AnalysisModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductPersistencePort {
    Page<AnalysisModel> findAll(Pageable pageable);
    AnalysisModel findById(Long id);
    List<AnalysisModel> findByName(String name);
    List<AnalysisModel> all();
    void deleteById(Long id);
    AnalysisModel update(AnalysisModel analysisModel, Long id);
    AnalysisModel save(AnalysisModel analysisModel);
    List<AnalysisModel> findAnalysisByMatrix(Long matrixId);
}
