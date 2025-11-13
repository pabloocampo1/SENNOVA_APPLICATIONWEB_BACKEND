package com.example.sennova.application.usecasesImpl;

import com.example.sennova.application.usecases.SampleUseCase;
import com.example.sennova.domain.model.testRequest.SampleAnalysisModel;
import com.example.sennova.domain.model.testRequest.SampleModel;
import com.example.sennova.domain.port.SamplePersistencePort;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SampleServiceImpl implements SampleUseCase {

    private final SamplePersistencePort samplePersistencePort;

    @Autowired
    public SampleServiceImpl(SamplePersistencePort samplePersistencePort) {
        this.samplePersistencePort = samplePersistencePort;
    }

    @Override
    public SampleModel save(SampleModel sampleModel) {
        return this.samplePersistencePort.save(sampleModel);
    }

    @Override
    public List<SampleModel> getAllByTestRequest(Long testRequest) {
        return this.samplePersistencePort.getAllByTestRequest(testRequest);
    }

    @Override
    @Transactional
    public void deleteById(Long sampleId) {
         this.samplePersistencePort.deleteById(sampleId);
    }

    @Override
    public List<SampleAnalysisModel> getAllAnalysisBySample(Long sampleId) {
        return List.of();
    }

}
