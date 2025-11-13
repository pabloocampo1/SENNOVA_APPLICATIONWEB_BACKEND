package com.example.sennova.infrastructure.persistence.repositoryJpa;

import com.example.sennova.infrastructure.persistence.entities.inventoryEquipmentEntities.MaintenanceRecordsEquipment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface MaintenanceEquipmentRepositoryJpa extends JpaRepository<MaintenanceRecordsEquipment, Long> {
    List<MaintenanceRecordsEquipment> findByEquipment_EquipmentId(Long equipmentId);

    @Modifying
    @Query("DELETE FROM MaintenanceRecordsEquipment m WHERE m.equipment.equipmentId = :equipmentId")
    void deleteByEquipmentId(Long equipmentId);
}
