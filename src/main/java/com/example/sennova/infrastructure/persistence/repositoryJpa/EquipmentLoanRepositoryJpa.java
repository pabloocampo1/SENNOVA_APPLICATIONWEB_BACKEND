package com.example.sennova.infrastructure.persistence.repositoryJpa;

import com.example.sennova.infrastructure.persistence.entities.inventoryEquipmentEntities.EquipmentLoanEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EquipmentLoanRepositoryJpa extends JpaRepository<EquipmentLoanEntity, Long> {

    List<EquipmentLoanEntity> findByEquipment_EquipmentId(Long equipmentId);

    @Modifying
    @Query("DELETE FROM EquipmentLoanEntity e WHERE e.equipment.equipmentId = :equipmentId")
    void deleteByEquipmentId(Long equipmentId);

}
