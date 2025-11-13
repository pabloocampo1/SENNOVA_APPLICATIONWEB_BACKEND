package com.example.sennova.application.usecases;

import com.example.sennova.domain.model.testRequest.SampleAnalysisModel;

import java.util.List;

public interface SampleAnalysisUseCase {
   List<SampleAnalysisModel> getSamplesAnalysisBySample(Long sampleId);
   void deleteById(Long id);
    SampleAnalysisModel save(SampleAnalysisModel sampleAnalysisModel);
}
