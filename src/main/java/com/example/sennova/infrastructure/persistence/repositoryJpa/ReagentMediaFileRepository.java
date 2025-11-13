package com.example.sennova.infrastructure.persistence.repositoryJpa;

import com.example.sennova.infrastructure.persistence.entities.inventoryReagentsEntities.ReagentMediaFilesEntity;
import com.example.sennova.infrastructure.persistence.entities.inventoryReagentsEntities.ReagentsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReagentMediaFileRepository extends JpaRepository<ReagentMediaFilesEntity, Long> {

    List<ReagentMediaFilesEntity> findAllByReagentEntity_ReagentsId(Long reagentId);
    ReagentMediaFilesEntity findByPublicId(String publicId);

}
