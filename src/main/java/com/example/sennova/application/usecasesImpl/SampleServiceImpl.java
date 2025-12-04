package com.example.sennova.application.usecasesImpl;

import com.example.sennova.application.dto.testeRequest.ReceptionInfoRequest;
import com.example.sennova.application.dto.testeRequest.SampleAnalysisRequestRecord;
import com.example.sennova.application.dto.testeRequest.SampleData;
import com.example.sennova.application.usecases.SampleUseCase;
import com.example.sennova.domain.constants.TestRequestConstants;
import com.example.sennova.domain.event.AnalysisResultSavedEvent;
import com.example.sennova.domain.event.DomainEventPublisher;
import com.example.sennova.domain.event.SampleReceptionUpdateEvent;
import com.example.sennova.domain.model.testRequest.SampleAnalysisModel;
import com.example.sennova.domain.model.testRequest.SampleModel;
import com.example.sennova.domain.port.SampleAnalysisPersistencePort;
import com.example.sennova.domain.port.SamplePersistencePort;

import com.example.sennova.infrastructure.persistence.entities.analysisRequestsEntities.SampleAnalysisEntity;
import com.example.sennova.infrastructure.persistence.entities.analysisRequestsEntities.SampleProductDocumentResult;
import com.example.sennova.infrastructure.restTemplate.CloudinaryService;
import com.example.sennova.web.exception.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class SampleServiceImpl implements SampleUseCase {

    private final SamplePersistencePort samplePersistencePort;
    private final SampleAnalysisPersistencePort sampleAnalysisPersistencePort;
    private final DomainEventPublisher domainEventPublisher;
    private final CloudinaryService cloudinaryService;

    @Autowired
    public SampleServiceImpl(SamplePersistencePort samplePersistencePort, SampleAnalysisPersistencePort sampleAnalysisPersistencePort, DomainEventPublisher domainEventPublisher, CloudinaryService cloudinaryService) {
        this.samplePersistencePort = samplePersistencePort;
        this.sampleAnalysisPersistencePort = sampleAnalysisPersistencePort;
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
    public List<SampleModel> getAllByStatusReception() {
        return this.samplePersistencePort.findAllByStatusReception();
    }

    @Override
    @Transactional
    public void deleteFileResultAnalysis(Long sampleProductDocumentResultId) {
          // find first the entity
        SampleProductDocumentResult documentResult =  this.sampleAnalysisPersistencePort.findDocumentResult(sampleProductDocumentResultId)
                .orElseThrow();

        try{
           Map resultDelete = this.cloudinaryService.deleteFile(documentResult.getPublicId());

           if(resultDelete.get("result").equals("ok")){
               this.sampleAnalysisPersistencePort.deleteAnalysisDocument(sampleProductDocumentResultId);
           }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public SampleModel getById(Long id) {
        SampleModel sample = this.samplePersistencePort.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No se encontro el id de la muestra : " + id));
        return sample;
    }

    @Override
    public SampleAnalysisModel saveResult( SampleAnalysisRequestRecord sampleAnalysisRequestRecord, String requestCode) {
        // save the result first
        if (sampleAnalysisRequestRecord.resultFinal().isEmpty() || sampleAnalysisRequestRecord.resultFinal().equals(" ")){
                  throw  new IllegalArgumentException("No puedes guardar el analisis sin un resultado final");
        }

        SampleAnalysisModel sampleAnalysisModelSaved =  this.sampleAnalysisPersistencePort.saveResult(sampleAnalysisRequestRecord);

        this.domainEventPublisher.publish(new AnalysisResultSavedEvent(requestCode));

        return sampleAnalysisModelSaved;
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

        // generate the due date for this sample
        sample.setDueDate(LocalDate.now().plusDays(15));

        
        SampleModel sampleSaved =  this.samplePersistencePort.save(sample);

        this.domainEventPublisher.publish(new SampleReceptionUpdateEvent(sampleSaved.getSampleId(), sampleSaved.getTestRequest().getTestRequestId()));

        return sampleSaved;
    }

    @Override
    public List<SampleAnalysisModel> getAllAnalysisBySample(Long sampleId) {
        return List.of();
    }

    @Override
    @Transactional
    public List<SampleProductDocumentResult> saveDocsResult(List<MultipartFile> docs, Long analysisResult) {

        if(docs.isEmpty()){
            throw  new IllegalArgumentException("No puedes realizar esta accion si no subes archivos.");
        }

        SampleAnalysisEntity sampleAnalysisEntity = this.sampleAnalysisPersistencePort.findEntityById(analysisResult)
                .orElseThrow(() -> new EntityNotFoundException("No se encontro el analysis en la base de datos."));


        List<SampleProductDocumentResult> currentListDocs = sampleAnalysisEntity.getSampleProductDocumentResult();

        for (MultipartFile file : docs){

            Map<String, String> fileUpload = this.cloudinaryService.uploadFile(file);

            String url = fileUpload.get("secure_url");
            String nameFile = fileUpload.get("originalFilename");
            String publicId = fileUpload.get("public_id");

            SampleProductDocumentResult newDoc = new SampleProductDocumentResult();
            newDoc.setUrl(url);
            newDoc.setNameFile(nameFile);
            newDoc.setPublicId(publicId);
            newDoc.setSampleProductAnalysis(sampleAnalysisEntity);

            currentListDocs.add(newDoc);

        }

        sampleAnalysisEntity.setSampleProductDocumentResult(currentListDocs);

       SampleAnalysisModel analysisResultSaved = this.sampleAnalysisPersistencePort.saveEntity(sampleAnalysisEntity);

        return analysisResultSaved.getSampleProductDocumentResult();
    }


}
