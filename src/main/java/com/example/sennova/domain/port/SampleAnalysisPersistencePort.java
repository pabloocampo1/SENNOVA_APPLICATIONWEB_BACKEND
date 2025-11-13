package com.example.sennova.domain.port;

import com.example.sennova.domain.model.testRequest.SampleAnalysisModel;

import java.util.List;

public interface SampleAnalysisPersistencePort {
    List<SampleAnalysisModel> findAllSamplesAnalysisBySample(Long sampleId);
    void deleteById(Long id);
    SampleAnalysisModel save(SampleAnalysisModel sampleAnalysisModel);
}
