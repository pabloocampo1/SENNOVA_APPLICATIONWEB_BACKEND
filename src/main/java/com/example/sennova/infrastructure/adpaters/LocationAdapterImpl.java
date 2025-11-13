package com.example.sennova.infrastructure.adpaters;

import com.example.sennova.domain.model.LocationModel;
import com.example.sennova.domain.port.LocationPersistencePort;
import com.example.sennova.infrastructure.mapperDbo.LocationMapperDbo;
import com.example.sennova.infrastructure.persistence.entities.LocationEntity;
import com.example.sennova.infrastructure.persistence.repositoryJpa.LocationPersistenceJpa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class LocationAdapterImpl implements LocationPersistencePort {

    private final LocationPersistenceJpa locationPersistenceJpa;
    private final LocationMapperDbo locationMapperDbo;

    @Autowired
    public LocationAdapterImpl(LocationPersistenceJpa locationPersistenceJpa, LocationMapperDbo locationMapperDbo) {
        this.locationPersistenceJpa = locationPersistenceJpa;
        this.locationMapperDbo = locationMapperDbo;
    }

    @Override
    public LocationModel save(LocationModel locationModel) {
        LocationEntity locationEntity = this.locationMapperDbo.toEntity(locationModel);
        LocationEntity locationEntitySaved = this.locationPersistenceJpa.save(locationEntity);
        return this.locationMapperDbo.toModel(locationEntitySaved);
    }

    @Override
    public LocationModel findById(Long id) {
        return this.locationMapperDbo.toModel(this.locationPersistenceJpa.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("No se encontro la ubicacion con id: " + id)));
    }

    @Override
    public LocationModel update(LocationModel locationModel, Long id) {

        // get the original entity to pass important data like created date
        LocationEntity locationEntityOriginal = this.locationPersistenceJpa.findById(id)
                .orElseThrow();

        // mapped the new entity
        LocationEntity locationEntity = this.locationMapperDbo.toEntity(locationModel);

        //  add the attributes of the original entity
        locationEntity.setCreateAt(locationEntityOriginal.getCreateAt());

        // save and return
        LocationEntity locationEntityUpdate = this.locationPersistenceJpa.save(locationEntity);
        return this.locationMapperDbo.toModel(locationEntityUpdate);
    }

    @Override
    public Page<LocationModel> findAllPage(Pageable pageable) {
        Page<LocationEntity> page = this.locationPersistenceJpa.findAll(pageable);
        return page.map(this.locationMapperDbo::toModel);
    }

    @Override
    public void deleteById(Long id) {
            this.locationPersistenceJpa.deleteById(id);
    }

    @Override
    public Boolean existById(Long id) {
        return this.locationPersistenceJpa.existsById(id);
    }

    @Override
    public List<LocationModel> findAllByName(String name) {
        List<LocationEntity> allByName = this.locationPersistenceJpa.findAllByLocationNameContainingIgnoreCase(name);
        return allByName
                .stream()
                .map(this.locationMapperDbo::toModel)
                .toList();
    }

    @Override
    public List<LocationModel> findAll() {
        List<LocationEntity> allEntities = this.locationPersistenceJpa.findAll();
        List<LocationModel> allModels = allEntities.stream().map(this.locationMapperDbo::toModel).toList();
        return allModels;
    }
}
