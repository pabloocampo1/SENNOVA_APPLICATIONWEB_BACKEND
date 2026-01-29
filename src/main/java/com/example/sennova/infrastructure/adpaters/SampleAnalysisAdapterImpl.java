package com.example.sennova.infrastructure.adpaters;

import com.example.sennova.application.dto.testeRequest.SampleAnalysisRequestRecord;
import com.example.sennova.domain.model.testRequest.SampleAnalysisModel;
import com.example.sennova.domain.port.SampleAnalysisPersistencePort;
import com.example.sennova.infrastructure.mapperDbo.SampleAnalysisMapperDbo;
import com.example.sennova.infrastructure.mapperDbo.SampleMapperDbo;
import com.example.sennova.infrastructure.persistence.entities.analysisRequestsEntities.SampleAnalysisEntity;
import com.example.sennova.infrastructure.persistence.entities.analysisRequestsEntities.SampleEntity;
import com.example.sennova.infrastructure.persistence.entities.analysisRequestsEntities.SampleProductDocumentResult;
import com.example.sennova.infrastructure.persistence.repositoryJpa.AnalysisDocumentRepositoryJpa;
import com.example.sennova.infrastructure.persistence.repositoryJpa.SampleAnalysisRepositoryJpa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class SampleAnalysisAdapterImpl implements SampleAnalysisPersistencePort {

    private final SampleAnalysisRepositoryJpa sampleAnalysisRepositoryJpa;
    private final SampleAnalysisMapperDbo sampleAnalysisMapperDbo;
    private final AnalysisDocumentRepositoryJpa analysisDocumentRepositoryJpa;

    @Autowired
    public SampleAnalysisAdapterImpl(SampleAnalysisRepositoryJpa sampleAnalysisRepositoryJpa, SampleAnalysisMapperDbo sampleAnalysisMapperDbo, AnalysisDocumentRepositoryJpa analysisDocumentRepositoryJpa) {
        this.sampleAnalysisRepositoryJpa = sampleAnalysisRepositoryJpa;
        this.sampleAnalysisMapperDbo = sampleAnalysisMapperDbo;
        this.analysisDocumentRepositoryJpa = analysisDocumentRepositoryJpa;
    }

    @Override
    public List<SampleAnalysisModel> findAllSamplesAnalysisBySample(Long sampleId) {

        return this.sampleAnalysisRepositoryJpa.findAllBySampleId(sampleId).stream().map(this.sampleAnalysisMapperDbo::toModel).toList();
    }

    @Override
    public void deleteById(Long id) {
        this.sampleAnalysisRepositoryJpa.deleteById(id);
    }

    @Override
    public void deleteAnalysisDocument(Long sampleProductDocumentResultId) {
        this.analysisDocumentRepositoryJpa.deleteById(sampleProductDocumentResultId);
    }

    @Override
    public SampleAnalysisModel save(SampleAnalysisModel sampleAnalysisModel) {
        SampleAnalysisEntity sampleAnalysisEntity = this.sampleAnalysisRepositoryJpa.save(this.sampleAnalysisMapperDbo.toEntity(sampleAnalysisModel));
        return this.sampleAnalysisMapperDbo.toModel(sampleAnalysisEntity);
    }

    @Override
    public Optional<SampleAnalysisModel> findById(Long sampleAnalysisModelId) {
        Optional<SampleAnalysisEntity> sampleAnalysisEntity = this.sampleAnalysisRepositoryJpa.findById(sampleAnalysisModelId);
        return sampleAnalysisEntity.map(this.sampleAnalysisMapperDbo::toModel);
    }

    @Override
    public Optional<SampleAnalysisEntity> findEntityById(Long sampleAnalysisId) {
        return  this.sampleAnalysisRepositoryJpa.findById(sampleAnalysisId);
    }



    @Override
    public SampleAnalysisModel saveEntity(SampleAnalysisEntity sampleAnalysisEntity) {
        return this.sampleAnalysisMapperDbo.toModel(
                this.sampleAnalysisRepositoryJpa.save(
                        sampleAnalysisEntity));
    }

    @Override
    public Optional<SampleProductDocumentResult> findDocumentResult(Long sampleProductDocumentResultId) {
        return this.analysisDocumentRepositoryJpa.findById(sampleProductDocumentResultId);
    }

    @Override
    public String findRequestCodeByAnalysis(Long sampleProductAnalysisId) {
        return this.sampleAnalysisRepositoryJpa.findSample(sampleProductAnalysisId);
    }

    @Override
    public Boolean findSampleReceptionByAnalysisId(Long sampleAnalysisId) {
        return this.sampleAnalysisRepositoryJpa.findStatusSampleReceptionByAnalysisId(sampleAnalysisId);
    }
}
