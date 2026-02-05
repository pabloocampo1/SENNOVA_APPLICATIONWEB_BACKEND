package com.example.sennova.infrastructure.persistence.repositoryJpa;

import com.example.sennova.infrastructure.persistence.entities.Analisys.MatrixEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MatrixRepositoryJpa extends JpaRepository<MatrixEntity, Long> {
    List<MatrixEntity> findAllByAvailableTrue();
    List<MatrixEntity> findAllByMatrixIdInAndAvailableTrue(List<Long> ids);
}
