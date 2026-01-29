package com.example.sennova.infrastructure.persistence.repositoryJpa;

import com.example.sennova.infrastructure.persistence.entities.analysisRequestsEntities.SampleAnalysisEntity;
import com.example.sennova.infrastructure.persistence.entities.analysisRequestsEntities.SampleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SampleAnalysisRepositoryJpa extends JpaRepository<SampleAnalysisEntity, Long> {

        @Query("SELECT s FROM SampleAnalysisEntity s WHERE s.sample.sampleId = :sampleId")
        List<SampleAnalysisEntity> findAllBySampleId(@Param("sampleId") Long sampleId);

        @Query(value = "SELECT\n" +
                "    t.request_code\n" +
                "FROM test_request t\n" +
                "INNER JOIN sample s\n" +
                "    ON s.test_request_id = t.test_request_id\n" +
                "INNER JOIN sample_product_analysis a\n" +
                "    ON s.sample_id = a.sample_id\n" +
                "WHERE a.sample_product_analysis_id = :analysisId\n", nativeQuery = true)
        String findSample(@Param("analysisId") Long analysisId);


        @Query(value = "SELECT s.status_reception FROM sample s INNER JOIN sample_product_analysis a ON a.sample_id = s.sample_id WHERE a.sample_product_analysis_id = :analysisId", nativeQuery = true)
        Boolean findStatusSampleReceptionByAnalysisId(@Param("analysisId") Long analysisId);
}
