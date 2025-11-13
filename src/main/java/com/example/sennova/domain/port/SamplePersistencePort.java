package com.example.sennova.domain.port;

import com.example.sennova.domain.model.testRequest.SampleModel;

import java.util.List;

public interface SamplePersistencePort {
    
    SampleModel save(SampleModel sampleModel);
    List<SampleModel> getAllByTestRequest(Long testRequest);
    void deleteById(Long sampleId);
}
