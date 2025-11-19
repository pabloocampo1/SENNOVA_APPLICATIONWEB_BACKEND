package com.example.sennova.application.usecasesImpl;

import com.example.sennova.application.dto.testeRequest.ReceptionInfoRequest;
import com.example.sennova.application.dto.testeRequest.SampleData;
import com.example.sennova.application.usecases.SampleUseCase;
import com.example.sennova.domain.model.testRequest.SampleAnalysisModel;
import com.example.sennova.domain.model.testRequest.SampleModel;
import com.example.sennova.domain.port.SamplePersistencePort;

import com.example.sennova.web.exception.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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
    public SampleModel getById(Long id) {
        SampleModel sample = this.samplePersistencePort.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No se encontro el id de la muestra : " + id));
        return sample;
    }

    @Override
    public SampleModel saveReception(ReceptionInfoRequest receptionInfoRequest, Long sampleId) {
        SampleModel sample = this.samplePersistencePort.findById(sampleId).orElseThrow(() -> new EntityNotFoundException("No se encontro la muestra con id : " + sampleId ));
        
        sample.setStatusReception(true);
        sample.setGross_weight(receptionInfoRequest.grossWeight());
        sample.setTemperature(receptionInfoRequest.temperature());
        sample.setIdentificationSample(receptionInfoRequest.identificationSample());
        sample.setObservations(receptionInfoRequest.observations());
        sample.setSampleReceptionDate(receptionInfoRequest.sampleReceptionDate());
        sample.setStorageConditions(receptionInfoRequest.storageConditions());
        sample.setSampleEntryDate(receptionInfoRequest.sampleEntryDate());
        sample.setPackageDescription(receptionInfoRequest.packageDescription());
        return this.samplePersistencePort.save(sample);
    }

    @Override
    public List<SampleAnalysisModel> getAllAnalysisBySample(Long sampleId) {
        return List.of();
    }



}
