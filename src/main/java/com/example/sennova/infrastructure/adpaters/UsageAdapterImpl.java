package com.example.sennova.infrastructure.adpaters;

import com.example.sennova.domain.model.UsageModel;
import com.example.sennova.domain.port.UsagePersistencePort;
import com.example.sennova.infrastructure.mapperDbo.UsageMapperDbo;
import com.example.sennova.infrastructure.persistence.entities.UsageEntity;
import com.example.sennova.infrastructure.persistence.repositoryJpa.UsageRepositoryJpa;
import com.example.sennova.web.exception.ResourceNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UsageAdapterImpl implements UsagePersistencePort {
    private final UsageMapperDbo usageMapperDbo;
    private final UsageRepositoryJpa usageRepositoryJpa;

    @Autowired
    public UsageAdapterImpl(UsageMapperDbo usageMapperDbo, UsageRepositoryJpa usageRepositoryJpa) {
        this.usageMapperDbo = usageMapperDbo;
        this.usageRepositoryJpa = usageRepositoryJpa;
    }

    @Override
    public UsageModel save(@Valid UsageModel usageModel) {
        UsageEntity equipmentLocationEntity = this.usageMapperDbo.toEntity(usageModel);
        UsageEntity usageEntitySaved = this.usageRepositoryJpa.save(equipmentLocationEntity);
        return this.usageMapperDbo.toModel(usageEntitySaved);
    }

    @Override
    public UsageModel update(UsageModel usageModel, Long id) {

        // get the original entity for get data important
        UsageEntity usageEntityOriginal = this.usageRepositoryJpa.findById(id)
                .orElseThrow();

        // change the model to entity
        UsageEntity usageEntity = this.usageMapperDbo.toEntity(usageModel);
        usageEntity.setCreateAt(usageEntityOriginal.getCreateAt());

        UsageEntity usageEntitySaved = this.usageRepositoryJpa.save(usageEntity);
        return this.usageMapperDbo.toModel(usageEntitySaved);
    }

    @Override
    public List<UsageModel> findAll() {
        List<UsageEntity> usageEntityList = this.usageRepositoryJpa.findAll();
        return usageEntityList.stream().map(this.usageMapperDbo::toModel).toList();
    }

    @Override
    public Page<UsageModel> findAll(Pageable pageable) {
        return this.usageRepositoryJpa.findAll(pageable).map(this.usageMapperDbo::toModel);
    }

    @Override
    public boolean existsById(Long id) {
        return this.usageRepositoryJpa.existsById(id);
    }

    @Override
    public List<UsageModel> findAllByName(String name) {
        List<UsageEntity> usageEntityList = this.usageRepositoryJpa.findAllByUsageNameContainingIgnoreCase(name);
        return usageEntityList.stream().map(this.usageMapperDbo::toModel).toList();
    }

    @Override
    public UsageModel findById(Long id) {
       UsageEntity usageEntity =  this.usageRepositoryJpa.findById(id)
               .orElseThrow(() -> new ResourceNotFoundException("No se pudo encontrar el uso,  con id : " + id));
        return this.usageMapperDbo.toModel(usageEntity);
    }

    @Override
    public void deleteById(Long id) {
            this.usageRepositoryJpa.deleteById(id);
    }
}
