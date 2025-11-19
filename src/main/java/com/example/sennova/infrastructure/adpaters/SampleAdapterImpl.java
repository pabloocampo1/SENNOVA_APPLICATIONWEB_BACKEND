package com.example.sennova.infrastructure.adpaters;

import com.example.sennova.domain.model.testRequest.SampleModel;
import com.example.sennova.domain.port.SamplePersistencePort;
import com.example.sennova.infrastructure.mapperDbo.SampleMapperDbo;
import com.example.sennova.infrastructure.persistence.entities.analysisRequestsEntities.SampleEntity;
import com.example.sennova.infrastructure.persistence.repositoryJpa.SampleRepositoryJpa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class SampleAdapterImpl implements SamplePersistencePort {

    private final SampleRepositoryJpa sampleRepositoryJpa;
    private final SampleMapperDbo sampleMapperDbo;

    @Autowired
    public SampleAdapterImpl(SampleRepositoryJpa sampleRepositoryJpa, SampleMapperDbo sampleMapperDbo) {
        this.sampleRepositoryJpa = sampleRepositoryJpa;
        this.sampleMapperDbo = sampleMapperDbo;
    }

    @Override
    public SampleModel save(SampleModel sampleModel) {
        SampleEntity sampleEntity = this.sampleRepositoryJpa.save(this.sampleMapperDbo.toEntity(sampleModel));
        return this.sampleMapperDbo.toModel(sampleEntity);
    }

    @Override
    public List<SampleModel> getAllByTestRequest(Long testRequest) {
        List<SampleModel> samples = this.sampleRepositoryJpa.findAllByTestRequest(testRequest)
                .stream()
                .map(this.sampleMapperDbo::toModel)
                .toList();
        return samples;
    }

    @Override
    public void deleteById(Long sampleId) {
        this.sampleRepositoryJpa.deleteById(sampleId);
    }

    @Override
    public Optional<SampleModel> findById(Long id) {
        Optional<SampleEntity> sampleEntity = this.sampleRepositoryJpa.findById(id);
        return sampleEntity.map(sample -> this.sampleMapperDbo.toModel(sample))  ;
    }
}
