package com.example.sennova.infrastructure.adpaters;

import com.example.sennova.domain.model.EquipmentLoanModel;
import com.example.sennova.domain.port.EquipmentLoanPersistencePort;
import com.example.sennova.infrastructure.mapperDbo.EquipmentLoanMapperDbo;
import com.example.sennova.infrastructure.persistence.entities.inventoryEquipmentEntities.EquipmentEntity;
import com.example.sennova.infrastructure.persistence.entities.inventoryEquipmentEntities.EquipmentLoanEntity;
import com.example.sennova.infrastructure.persistence.repositoryJpa.EquipmentLoanRepositoryJpa;
import com.example.sennova.infrastructure.persistence.repositoryJpa.EquipmentMediaRepositoryJpa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class EquipmentLoanAdapterImpl implements EquipmentLoanPersistencePort {

    private final EquipmentLoanRepositoryJpa equipmentLoanRepositoryJpa;
    private final EquipmentLoanMapperDbo equipmentLoanMapperDbo;


    @Autowired
    public EquipmentLoanAdapterImpl(EquipmentLoanRepositoryJpa equipmentLoanRepositoryJpa, EquipmentLoanMapperDbo equipmentLoanMapperDbo) {
        this.equipmentLoanRepositoryJpa = equipmentLoanRepositoryJpa;
        this.equipmentLoanMapperDbo = equipmentLoanMapperDbo;
    }


    @Override
    public EquipmentLoanModel save(EquipmentLoanModel equipmentLoanModel) {
        EquipmentLoanEntity equipmentLoanEntity = this.equipmentLoanMapperDbo.toEntity(equipmentLoanModel);
        EquipmentLoanEntity equipmentLoanSaved = this.equipmentLoanRepositoryJpa.save(equipmentLoanEntity);
        return this.equipmentLoanMapperDbo.toModel(equipmentLoanSaved);
    }

    @Override
    public List<EquipmentLoanModel> findAllByEquipmentId(Long equipmentId) {

        List<EquipmentLoanEntity> equipmentLoanEntityList = this.equipmentLoanRepositoryJpa.findByEquipment_EquipmentId(equipmentId);
        return equipmentLoanEntityList.stream().map(this.equipmentLoanMapperDbo::toModel).toList();

    }

    @Override
    public Boolean deleteById(Long id) {
        this.equipmentLoanRepositoryJpa.deleteById(id);
        return true;
    }

    @Override
    public EquipmentLoanModel getById(Long id) {
        EquipmentLoanEntity equipmentLoanEntity = this.equipmentLoanRepositoryJpa.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No se encontro el prestamo con ese id"));
        return this.equipmentLoanMapperDbo.toModel(equipmentLoanEntity);
    }

    @Override
    public Boolean existsById(Long id) {
        return this.equipmentLoanRepositoryJpa.existsById(id);
    }

    @Override
    public void deleteByEquipmentId(Long equipmentId) {
        this.equipmentLoanRepositoryJpa.deleteByEquipmentId(equipmentId);
    }
}
