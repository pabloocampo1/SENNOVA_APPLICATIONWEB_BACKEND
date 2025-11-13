package com.example.sennova.application.usecasesImpl;

import com.example.sennova.application.usecases.LocationUseCase;
import com.example.sennova.domain.model.EquipmentModel;
import com.example.sennova.domain.model.LocationModel;
import com.example.sennova.domain.model.ReagentModel;
import com.example.sennova.domain.port.EquipmentPersistencePort;
import com.example.sennova.domain.port.LocationPersistencePort;
import com.example.sennova.domain.port.ReagentPersistencePort;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationServiceImpl implements LocationUseCase {

    private final LocationPersistencePort locationPersistencePort;
    private final EquipmentPersistencePort equipmentPersistencePort;
    private final ReagentPersistencePort reagentPersistencePort;

    @Autowired
    public LocationServiceImpl(LocationPersistencePort locationPersistencePort, EquipmentPersistencePort equipmentPersistencePort, ReagentPersistencePort reagentPersistencePort) {
        this.locationPersistencePort = locationPersistencePort;
        this.equipmentPersistencePort = equipmentPersistencePort;
        this.reagentPersistencePort = reagentPersistencePort;
    }

    @Override
    @Transactional
    public LocationModel save(@Valid LocationModel locationModel) {
        return this.locationPersistencePort.save(locationModel);
    }

    @Override
    @Transactional
    public LocationModel update(@Valid Long id, @Valid LocationModel locationModel) {
        if(!this.locationPersistencePort.existById(id)){
            throw new UsernameNotFoundException("No se pudo encontrar la ubicacion con id : " + id);
        }
        if(locationModel.getEquipmentLocationId() == null){
            throw new IllegalArgumentException("Ah ocurrido un error al intentar editar la ubicacion, por favor intentalo mas tarde, o informa sobre el error. : " + id);
        }

        return this.locationPersistencePort.update(locationModel, id);
    }

    @Override
    public List<LocationModel> getAllByName(@Valid String name) {
        return this.locationPersistencePort.findAllByName(name);
    }

    @Override
    public LocationModel getById(@Valid Long id) {
        if(!this.locationPersistencePort.existById(id)){
            throw new IllegalArgumentException("No se pudo encontrar la ubicacion con id : " + id);
        }
        return this.locationPersistencePort.findById(id);
    }

    @Override
    public void deleteById(Long id) {
        LocationModel locationModel = this.locationPersistencePort.findById(id);
        List<EquipmentModel> listEquipment = this.equipmentPersistencePort.findAllByLocation(locationModel);

        for (EquipmentModel equipment : listEquipment){
            equipment.setLocation(null);
            this.equipmentPersistencePort.save(equipment);
        }

        List<ReagentModel> reagentList = this.reagentPersistencePort.findAllByLocation(locationModel);
        for (ReagentModel reagent : reagentList){
            reagent.setLocation(null);
            this.reagentPersistencePort.save(reagent);
        }

        this.locationPersistencePort.deleteById(id);
    }

    @Override
    public Page<LocationModel> getAllPage(Pageable pageable) {
        return this.locationPersistencePort.findAllPage(pageable);
    }

    @Override
    public List<LocationModel> getAll() {
        return this.locationPersistencePort.findAll();
    }
}
