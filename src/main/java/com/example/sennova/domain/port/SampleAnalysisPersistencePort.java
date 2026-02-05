package com.example.sennova.domain.port;

import com.example.sennova.domain.model.testRequest.SampleAnalysisModel;
import com.example.sennova.infrastructure.persistence.entities.requestsEntities.SampleAnalysisEntity;
import com.example.sennova.infrastructure.persistence.entities.requestsEntities.SampleProductDocumentResult;

import java.util.List;
import java.util.Optional;

public interface SampleAnalysisPersistencePort {
    List<SampleAnalysisModel> findAllSamplesAnalysisBySample(Long sampleId);
    void deleteById(Long id);
    void deleteAnalysisDocument(Long sampleProductDocumentResultId);
    SampleAnalysisModel save(SampleAnalysisModel sampleAnalysisModel);
    Optional<SampleAnalysisModel> findById(Long sampleAnalysisModel);
    Optional<SampleAnalysisEntity> findEntityById(Long sampleAnalysisId) ;
   
    SampleAnalysisModel saveEntity(SampleAnalysisEntity sampleAnalysisEntity);
    Optional<SampleProductDocumentResult> findDocumentResult(Long sampleProductDocumentResultId);
    String findRequestCodeByAnalysis(Long sampleProductAnalysisId);
    Boolean findSampleReceptionByAnalysisId(Long  sampleAnalysisId);
}
