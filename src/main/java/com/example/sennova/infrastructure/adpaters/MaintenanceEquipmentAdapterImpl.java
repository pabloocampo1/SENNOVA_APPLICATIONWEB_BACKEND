package com.example.sennova.infrastructure.adpaters;

import com.example.sennova.domain.model.MaintenanceRecordEquipmentModel;
import com.example.sennova.domain.port.EquipmentPersistencePort;
import com.example.sennova.domain.port.MaintenanceEquipmentPersistencePort;
import com.example.sennova.infrastructure.mapperDbo.MaintenanceMapperDbo;
import com.example.sennova.infrastructure.persistence.entities.inventoryEquipmentEntities.EquipmentEntity;
import com.example.sennova.infrastructure.persistence.entities.inventoryEquipmentEntities.MaintenanceRecordsEquipment;
import com.example.sennova.infrastructure.persistence.repositoryJpa.EquipmentRepositoryJpa;
import com.example.sennova.infrastructure.persistence.repositoryJpa.MaintenanceEquipmentRepositoryJpa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MaintenanceEquipmentAdapterImpl implements MaintenanceEquipmentPersistencePort {

    private final MaintenanceEquipmentRepositoryJpa maintenanceEquipmentRepositoryJpa;
    private final MaintenanceMapperDbo maintenanceMapperDbo;
    private final EquipmentRepositoryJpa equipmentRepositoryJpa;

    public MaintenanceEquipmentAdapterImpl(MaintenanceEquipmentRepositoryJpa maintenanceEquipmentRepositoryJpa, MaintenanceMapperDbo maintenanceMapperDbo, EquipmentRepositoryJpa equipmentRepositoryJpa) {
        this.maintenanceEquipmentRepositoryJpa = maintenanceEquipmentRepositoryJpa;
        this.maintenanceMapperDbo = maintenanceMapperDbo;
        this.equipmentRepositoryJpa = equipmentRepositoryJpa;
    }


    @Override
    public MaintenanceRecordEquipmentModel save(MaintenanceRecordEquipmentModel model) {
        MaintenanceRecordsEquipment entity = maintenanceMapperDbo.toEntity(model);

        // asegurar que la entidad tiene el equipo seteado correctamente
        EquipmentEntity equipment = equipmentRepositoryJpa.findById(model.getEquipment().getEquipmentId())
                .orElseThrow(() -> new IllegalArgumentException("Equipo no encontrado"));
        entity.setEquipment(equipment);

        // guardar solo el mantenimiento (no el equipo)
        MaintenanceRecordsEquipment saved = maintenanceEquipmentRepositoryJpa.save(entity);

        return maintenanceMapperDbo.toModel(saved);
    }

    @Override
    public List<MaintenanceRecordEquipmentModel> findAllByEquipmentId(Long equipmentId) {
            List<MaintenanceRecordsEquipment> maintenanceRecordsEquipmentPage = this.maintenanceEquipmentRepositoryJpa.findByEquipment_EquipmentId(equipmentId);
            return  maintenanceRecordsEquipmentPage.stream().map(this.maintenanceMapperDbo::toModel).toList();
    }

    @Override
    public Boolean existById(Long id) {
        return this.maintenanceEquipmentRepositoryJpa.existsById(id);
    }

    @Override
    public Boolean deleteById(Long id) {
        if(!this.maintenanceEquipmentRepositoryJpa.existsById(id)){
            throw new IllegalArgumentException("No se encontro el mantenimiento con id: " + id);
        }

        this.maintenanceEquipmentRepositoryJpa.deleteById(id);
        return true;
    }

    @Override
    public void deleteByEquipmentId(Long equipmentId) {
            this.maintenanceEquipmentRepositoryJpa.deleteByEquipmentId(equipmentId);
    }
}
