package com.example.sennova.application.usecases;

import com.example.sennova.application.dto.testeRequest.ReceptionInfoRequest;
import com.example.sennova.application.dto.testeRequest.SampleData;
import com.example.sennova.domain.model.testRequest.SampleAnalysisModel;
import com.example.sennova.domain.model.testRequest.SampleModel;

import java.util.List;

public interface SampleUseCase {
    SampleModel save(SampleModel sampleModel);
    List<SampleModel> getAllByTestRequest(Long testRequest);
    void deleteById(Long sampleId);
    SampleModel getById(Long id);
    SampleModel saveReception(ReceptionInfoRequest receptionInfoRequest, Long sampleId);
    List<SampleAnalysisModel> getAllAnalysisBySample(Long sampleId);
}
