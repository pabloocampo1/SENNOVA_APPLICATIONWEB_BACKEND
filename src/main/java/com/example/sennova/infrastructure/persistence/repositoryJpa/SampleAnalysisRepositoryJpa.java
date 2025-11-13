package com.example.sennova.infrastructure.persistence.repositoryJpa;

import com.example.sennova.infrastructure.persistence.entities.analysisRequestsEntities.SampleAnalysisEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SampleAnalysisRepositoryJpa extends JpaRepository<SampleAnalysisEntity, Long> {

        @Query("SELECT s FROM SampleAnalysisEntity s WHERE s.sample.sampleId = :sampleId")
        List<SampleAnalysisEntity> findAllBySampleId(@Param("sampleId") Long sampleId);
}
