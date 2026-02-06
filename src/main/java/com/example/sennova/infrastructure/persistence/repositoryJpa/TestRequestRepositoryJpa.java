package com.example.sennova.infrastructure.persistence.repositoryJpa;

import com.example.sennova.infrastructure.persistence.entities.requestsEntities.TestRequestEntity;
import com.example.sennova.infrastructure.projection.SampleInfoSummaryTestRequestProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TestRequestRepositoryJpa extends JpaRepository<TestRequestEntity, Long> {

    @EntityGraph(attributePaths = {"sampleEntityList"})
    List<TestRequestEntity> findAllByOrderByCreateAtDesc();

    @Query(nativeQuery = true, value = "SELECT \n" +
            "    s.matrix AS sample,\n" +
            "    a.analysis_name AS analysis,\n" +
            "    a.price AS priceByAnalysis,\n" +
            "    COUNT(*) AS quantityAnalysisBySample,\n" +
            "    (a.price * COUNT(*)) AS total\n" +
            "FROM sample s\n" +
            "INNER JOIN sample_product_analysis spa ON spa.sample_id = s.sample_id\n" +
            "INNER JOIN analysis a ON a.analysis_id = spa.analysis_id\n" +
            "WHERE s.test_request_id = :testRequestId\n" +
            "GROUP BY s.matrix, a.analysis_name, a.price\n" +
            "ORDER BY s.matrix, a.analysis_name;")
    List<SampleInfoSummaryTestRequestProjection> findSamplesByTestRequest(@Param("testRequestId") Long testRequestId);

    List<TestRequestEntity> findAllByState(String param);

    List<TestRequestEntity> findAllByRequestCodeContainingIgnoreCase(String requestCode);

    List<TestRequestEntity> findAllByDueDate(LocalDate today);

    List<TestRequestEntity> findAllByIsApprovedTrueOrderByCreateAtDesc();
    Page<TestRequestEntity> findAllByIsApprovedTrueOrderByCreateAtDesc(Pageable pageable);

    List<TestRequestEntity> findAllByDueDateBefore(LocalDate today);

    Optional<TestRequestEntity> findByRequestCode(String requestCode);

    @Query("""
    SELECT COUNT(s) 
    FROM SampleEntity s 
    WHERE s.testRequest.testRequestId = :testRequestId 
      AND s.isDelivered = false
""")
    long countNotDeliveredSamples(@Param("testRequestId") Long testRequestId);


    List<TestRequestEntity> findAllByDeliveryStatusContainingIgnoreCase(String state);

    List<TestRequestEntity> findAllByCustomer_CustomerNameContainingIgnoreCase(@Param("customerName") String customerName);


    // This method is used inside an asynchronous event listener that is triggered
    // when a result is submitted. It validates whether all the analysis results
    // of every sample in a test request are completed.
    @Query("""
       SELECT tr FROM TestRequestEntity tr
       LEFT JOIN FETCH tr.sampleEntityList s
       LEFT JOIN FETCH s.analysisEntities a
       WHERE tr.requestCode = :requestCode
       """)
    Optional<TestRequestEntity> getWithSamplesAndAnalysis(String requestCode);

    @Query(value = "\n" +
            "SELECT *\n" +
            "FROM test_request t\n" +
            "WHERE YEAR(t.create_at) = :year AND t.state = \"ACEPTADA\"\n" +
            "ORDER BY t.create_at asc; ", nativeQuery = true)
    List<TestRequestEntity> findAllByYear(@Param("year") String year);

    @Query(value = "SELECT MAX(CAST(SUBSTRING_INDEX(request_code, '-', -1) AS UNSIGNED)) AS max_sequence\n" +
            "FROM test_request \n" +
            "WHERE request_code LIKE CONCAT(:year, '%');",
            nativeQuery = true) 
    Integer findMaxSequenceForYear(@Param("year") String year);


    

}
