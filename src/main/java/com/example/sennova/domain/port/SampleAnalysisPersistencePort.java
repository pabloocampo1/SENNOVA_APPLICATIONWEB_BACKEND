package com.example.sennova.domain.port;

import com.example.sennova.application.dto.testeRequest.SampleAnalysisRequestRecord;
import com.example.sennova.domain.model.testRequest.SampleAnalysisModel;

import java.util.List;
import java.util.Optional;

public interface SampleAnalysisPersistencePort {
    List<SampleAnalysisModel> findAllSamplesAnalysisBySample(Long sampleId);
    void deleteById(Long id);
    SampleAnalysisModel save(SampleAnalysisModel sampleAnalysisModel);
    Optional<SampleAnalysisModel> findById(Long sampleAnalysisModel);
    SampleAnalysisModel saveResult(SampleAnalysisRequestRecord sampleAnalysisRequestRecord) ;
}
