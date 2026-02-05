package com.example.sennova.infrastructure.adpaters;

import com.example.sennova.domain.model.testRequest.SampleModel;
import com.example.sennova.domain.port.SamplePersistencePort;
import com.example.sennova.infrastructure.mapperDbo.SampleMapperDbo;
import com.example.sennova.infrastructure.persistence.entities.requestsEntities.SampleEntity;
import com.example.sennova.infrastructure.persistence.repositoryJpa.SampleRepositoryJpa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
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
        return sampleEntity.map(this.sampleMapperDbo::toModel)  ;
    }

    @Override
    public List<SampleModel> findAllByStatusReceptionTrueAndNoExpiredAndIsDeliveredFalse() {
        List<SampleEntity> samples = this.sampleRepositoryJpa.findAllByStatusReceptionTrueAndIsDeliveredFalse();

        // return only the samples not expired
        List<SampleEntity> samplesEntities = samples.stream()
                .filter(s -> s.getDueDate().isAfter(LocalDate.now()) ||
                        s.getDueDate().isEqual(LocalDate.now()))
                .sorted(Comparator.comparing(SampleEntity::getDueDate))
                .toList();
        return samplesEntities.stream().map(this.sampleMapperDbo::toModel).toList();
    }

    @Override
    public List<SampleModel> findAllById(List<Long> samples) {
        List<SampleEntity> sampleEntities = this.sampleRepositoryJpa.findAllById(samples);
        return sampleEntities.stream().map(this.sampleMapperDbo::toModel).toList();
    }

    @Override
    public List<SampleModel> findAllByStatusDeliveryIsExpired(LocalDate now) {
        List<SampleEntity> samples = this.sampleRepositoryJpa.findAllExpiredAndNotDelivered(now);
        return samples.stream().map(this.sampleMapperDbo::toModel).toList();
    }

    @Override
    public Page<SampleModel> findAllSamplesDeliveredTrue(Pageable pageable) {
        Page<SampleEntity> samples = this.sampleRepositoryJpa.findAllByIsDeliveredTrue(pageable);
        return samples.map(this.sampleMapperDbo::toModel);
    }

    @Override
    public Page<SampleModel> findAllWithoutReception(Pageable pageable) {
        Page<SampleEntity> samples = this.sampleRepositoryJpa.findAllByStatusReceptionFalse(pageable);
        return samples.map(this.sampleMapperDbo::toModel);
    }

    @Override
    public Optional<SampleModel> findSampleByAnalysisId(Long analysisId) {

        Optional<SampleEntity> sampleEntity = this.sampleRepositoryJpa.findSampleByAnalysisId(analysisId);
        return sampleEntity.map(this.sampleMapperDbo::toModel)  ;
    }

    @Override
    public Integer findMaxSampleSequenceByYear(String shortYear) {
        return this.sampleRepositoryJpa.findMaxSampleSequenceByYear(shortYear);
    }
}
