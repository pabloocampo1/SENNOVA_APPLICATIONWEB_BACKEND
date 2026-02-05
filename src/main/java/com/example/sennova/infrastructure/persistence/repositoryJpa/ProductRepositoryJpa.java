package com.example.sennova.infrastructure.persistence.repositoryJpa;

import com.example.sennova.infrastructure.persistence.entities.Analisys.AnalysisEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepositoryJpa extends JpaRepository<AnalysisEntity, Long> {

    List<AnalysisEntity> findAllByAnalysisNameContainingIgnoreCase(String name);
    List<AnalysisEntity> findAllByAvailableTrue();

    @Query(value = "SELECT a.* FROM analysis a INNER JOIN matrix_analysis m ON m.analysis_id = a.analysis_id WHERE m.matrix_id = :matrixId", nativeQuery = true)
    List<AnalysisEntity> findAnalysisByMatrix(@Param("matrixId") Long matrix);
}
