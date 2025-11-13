package com.example.sennova.infrastructure.persistence.repositoryJpa;

import com.example.sennova.infrastructure.persistence.entities.analysisRequestsEntities.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepositoryJpa extends JpaRepository<ProductEntity, Long> {

    List<ProductEntity> findAllByAnalysisContainingIgnoreCase(String name);
    List<ProductEntity> findAllByAvailableTrue();
}
