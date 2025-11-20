package com.example.sennova.application.usecasesImpl;

import com.example.sennova.application.dto.testeRequest.ReceptionInfoRequest;
import com.example.sennova.application.dto.testeRequest.SampleData;
import com.example.sennova.application.usecases.SampleUseCase;
import com.example.sennova.domain.event.DomainEventPublisher;
import com.example.sennova.domain.event.SampleReceptionUpdateEvent;
import com.example.sennova.domain.model.testRequest.SampleAnalysisModel;
import com.example.sennova.domain.model.testRequest.SampleModel;
import com.example.sennova.domain.port.SamplePersistencePort;

import com.example.sennova.infrastructure.restTemplate.CloudinaryService;
import com.example.sennova.web.exception.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
public class SampleServiceImpl implements SampleUseCase {

    private final SamplePersistencePort samplePersistencePort;
    private final DomainEventPublisher domainEventPublisher;
    private final CloudinaryService cloudinaryService;

    @Autowired
    public SampleServiceImpl(SamplePersistencePort samplePersistencePort, DomainEventPublisher domainEventPublisher, CloudinaryService cloudinaryService) {
        this.samplePersistencePort = samplePersistencePort;
        this.domainEventPublisher = domainEventPublisher;
        this.cloudinaryService = cloudinaryService;
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
    @Transactional
    public SampleModel saveReception(@Valid  ReceptionInfoRequest receptionInfoRequest, @Valid Long sampleId, MultipartFile file) {
        SampleModel sample = this.samplePersistencePort.findById(sampleId).orElseThrow(() -> new EntityNotFoundException("No se encontro la muestra con id : " + sampleId ));


        // if have image upload
        String imageUrl = "";
        if(file != null){
            try {
                 imageUrl = this.cloudinaryService.uploadImage(file);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }


        // Delete the previous reception image if it exists.
        // If the sample already has a reception image and a new one arrives,
        // remove the old image from Cloudinary to avoid leaving unused files.

        if (sample.getSampleImage() != null && !sample.getSampleImage().isEmpty()) {
            try {
                this.cloudinaryService.deleteFileByUrl(sample.getSampleImage());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }



        sample.setStatusReception(true);
        sample.setGross_weight(receptionInfoRequest.grossWeight());
        sample.setTemperature(receptionInfoRequest.temperature());
        sample.setIdentificationSample(receptionInfoRequest.identificationSample());
        sample.setObservations(receptionInfoRequest.observations());
        sample.setSampleReceptionDate(receptionInfoRequest.sampleReceptionDate());
        sample.setStorageConditions(receptionInfoRequest.storageConditions());
        sample.setSampleEntryDate(receptionInfoRequest.sampleEntryDate());
        sample.setPackageDescription(receptionInfoRequest.packageDescription());
        sample.setSampleImage(imageUrl);

        
        SampleModel sampleSaved =  this.samplePersistencePort.save(sample);

        this.domainEventPublisher.publish(new SampleReceptionUpdateEvent(sampleSaved.getSampleId(), sampleSaved.getTestRequest().getTestRequestId()));

        return sampleSaved;
    }

    @Override
    public List<SampleAnalysisModel> getAllAnalysisBySample(Long sampleId) {
        return List.of();
    }






}
