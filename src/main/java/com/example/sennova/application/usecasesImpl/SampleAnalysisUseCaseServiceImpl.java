package com.example.sennova.application.usecasesImpl;


import com.example.sennova.application.usecases.SampleAnalysisUseCase;
import com.example.sennova.domain.model.testRequest.SampleAnalysisModel;
import com.example.sennova.domain.port.SampleAnalysisPersistencePort;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SampleAnalysisUseCaseServiceImpl implements SampleAnalysisUseCase {

    private final SampleAnalysisPersistencePort sampleAnalysisPersistencePort;

    @Autowired
    public SampleAnalysisUseCaseServiceImpl(SampleAnalysisPersistencePort sampleAnalysisPersistencePort) {
        this.sampleAnalysisPersistencePort = sampleAnalysisPersistencePort;
    }

    @Override
    public List<SampleAnalysisModel> getSamplesAnalysisBySample(Long sampleId) {
        return this.sampleAnalysisPersistencePort.findAllSamplesAnalysisBySample(sampleId);
    }

    @Override
    @Transactional
    public void deleteById( @Valid Long id) {
        this.sampleAnalysisPersistencePort.deleteById(id);
    }

    @Override
    public SampleAnalysisModel save(SampleAnalysisModel sampleAnalysisModel) {
        return this.sampleAnalysisPersistencePort.save(sampleAnalysisModel);
    }
}
