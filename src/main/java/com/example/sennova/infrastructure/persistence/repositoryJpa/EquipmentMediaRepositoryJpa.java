package com.example.sennova.infrastructure.persistence.repositoryJpa;

import com.example.sennova.infrastructure.persistence.entities.inventoryEquipmentEntities.EquipmentMediaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EquipmentMediaRepositoryJpa extends JpaRepository<EquipmentMediaEntity, Long> {

    @Query(value = "SELECT * FROM equipment_media WHERE equipment_media.equipment_id = :id ", nativeQuery = true)
    List<EquipmentMediaEntity> findByEquipmentId(@Param("id") Long id);

    Optional<EquipmentMediaEntity> findByPublicId(String public_id);
}
