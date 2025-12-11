package com.example.sennova.domain.port;

import com.example.sennova.domain.model.testRequest.SampleModel;

import java.util.List;
import java.util.Optional;

public interface SamplePersistencePort {
    
    SampleModel save(SampleModel sampleModel);
    List<SampleModel> getAllByTestRequest(Long testRequest);
    void deleteById(Long sampleId);
    Optional<SampleModel> findById(Long id);
    List<SampleModel> findAllByStatusReception();
    List<SampleModel> findAllById(List<Long> samples);
}
