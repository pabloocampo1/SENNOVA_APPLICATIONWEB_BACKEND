package com.example.sennova.infrastructure.adpaters;

import com.example.sennova.application.dto.inventory.ReagentInventory.ReagentSummaryStatistics;
import com.example.sennova.domain.constants.ReagentStateCons;
import com.example.sennova.domain.model.LocationModel;
import com.example.sennova.domain.model.ReagentModel;
import com.example.sennova.domain.model.UsageModel;
import com.example.sennova.domain.port.ReagentPersistencePort;
import com.example.sennova.infrastructure.mapperDbo.LocationMapperDbo;
import com.example.sennova.infrastructure.mapperDbo.ReagentMapperDbo;
import com.example.sennova.infrastructure.mapperDbo.UsageMapperDbo;
import com.example.sennova.infrastructure.persistence.entities.LocationEntity;
import com.example.sennova.infrastructure.persistence.entities.UsageEntity;
import com.example.sennova.infrastructure.persistence.entities.inventoryReagentsEntities.ReagentsEntity;
import com.example.sennova.infrastructure.persistence.repositoryJpa.ReagentRepositoryJpa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public class ReagentAdapterImpl implements ReagentPersistencePort {

    private final ReagentRepositoryJpa reagentRepositoryJpa;
    private final ReagentMapperDbo reagentMapperDbo;
    private final LocationMapperDbo locationMapperDbo;
    private final UsageMapperDbo usageMapperDbo;

    @Autowired
    public ReagentAdapterImpl(ReagentRepositoryJpa reagentRepositoryJpa, ReagentMapperDbo reagentMapperDbo, LocationMapperDbo locationMapperDbo, UsageMapperDbo usageMapperDbo) {
        this.reagentRepositoryJpa = reagentRepositoryJpa;
        this.reagentMapperDbo = reagentMapperDbo;
        this.locationMapperDbo = locationMapperDbo;
        this.usageMapperDbo = usageMapperDbo;
    }


    @Override
    public ReagentModel save(ReagentModel reagentModel) {
        ReagentsEntity reagentsEntity = this.reagentRepositoryJpa.save(this.reagentMapperDbo.toEntity(reagentModel));
        return this.reagentMapperDbo.toModel(reagentsEntity);
    }

    @Override
    public ReagentModel saveDirect(ReagentModel reagentModel) {
        return this.reagentMapperDbo.toModel(
                this.reagentRepositoryJpa.save(
                        this.reagentMapperDbo.toEntity(reagentModel)));
    }

    @Override
    public ReagentModel update(ReagentModel reagentModel) {
        return null;
    }

    @Override
    public List<ReagentModel> findAll() {
        List<ReagentsEntity> reagentsEntities = this.reagentRepositoryJpa.findAll();
        return reagentsEntities.stream().map(this.reagentMapperDbo::toModel).toList();
    }

    @Override
    public Page<ReagentModel> findAll(Pageable pageable) {
        Page<ReagentsEntity> reagentsEntities = this.reagentRepositoryJpa.findAll(pageable);
        return reagentsEntities.map(this.reagentMapperDbo::toModel);
    }

    @Override
    public List<ReagentModel> findAllByName(String name) {
        List<ReagentsEntity> reagentsEntities = this.reagentRepositoryJpa.findAllByReagentNameContainingIgnoreCase(name);
        System.out.println(reagentsEntities);
        return reagentsEntities.stream().map(this.reagentMapperDbo::toModel).toList();
    }

    @Override
    public List<ReagentModel> findAllByExpirationDate(LocalDate currentDate) {
        List<ReagentsEntity> reagentsEntityList = this.reagentRepositoryJpa.findAllByExpirationDate(currentDate);
        return reagentsEntityList.stream().map(this.reagentMapperDbo::toModel).toList();
    }

    @Override
    public List<ReagentModel> findAllByLocation(LocationModel locationModel) {
        LocationEntity locationEntity = this.locationMapperDbo.toEntity(locationModel);
        List<ReagentsEntity> reagentsEntities = this.reagentRepositoryJpa.findAllByLocation(locationEntity);
        return reagentsEntities.stream().map(this.reagentMapperDbo::toModel).toList();

    }

    @Override
    public List<ReagentModel> findAllByInventoryTag(String inventoryTag) {
        List<ReagentsEntity> reagentsEntities = this.reagentRepositoryJpa.findAllBySenaInventoryTagContainingIgnoreCase(inventoryTag);
        return reagentsEntities.stream().map(this.reagentMapperDbo::toModel).toList();
    }

    @Override
    public List<ReagentModel> findAllByUsage(UsageModel usageModel) {
        UsageEntity usage = this.usageMapperDbo.toEntity(usageModel);
        List<ReagentsEntity> entitiesByUsage = this.reagentRepositoryJpa.findAllByUsage(usage);
        return entitiesByUsage.stream().map(this.reagentMapperDbo::toModel).toList();
    }

    @Override
    public ReagentModel findById(Long id) {
        ReagentsEntity reagentsEntity = this.reagentRepositoryJpa.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No se pudo contrar el reactivo"));

        return this.reagentMapperDbo.toModel(reagentsEntity);
    }

    @Override
    public ReagentsEntity findEntityById(Long id) {
        return this.reagentRepositoryJpa.findById(id).orElseThrow(() -> new IllegalArgumentException("No se encontro el reactivo"));
    }

    @Override
    public void deleteById(Long id) {
        this.reagentRepositoryJpa.deleteById(id);
    }

    @Override
    public ReagentSummaryStatistics getSummaryStatics() {

        long all = this.reagentRepositoryJpa.count();
        long allByLowStock = this.reagentRepositoryJpa.countByQuantityLessThanEqual(0);
        long allExpired = this.reagentRepositoryJpa.countByStateExpiration(ReagentStateCons.STATE_EXPIRED);

        return new ReagentSummaryStatistics(all, allExpired, allByLowStock);
    }


}
