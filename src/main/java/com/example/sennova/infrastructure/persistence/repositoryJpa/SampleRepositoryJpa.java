package com.example.sennova.infrastructure.persistence.repositoryJpa;

import com.example.sennova.infrastructure.persistence.entities.analysisRequestsEntities.SampleEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface SampleRepositoryJpa extends JpaRepository<SampleEntity, Long> {
    @Query("SELECT s FROM SampleEntity s WHERE s.testRequest.testRequestId = :testRequestId")
    List<SampleEntity> findAllByTestRequest(@Param("testRequestId") Long testRequest);

    List<SampleEntity> findAllByStatusReceptionTrueAndIsDeliveredFalse();

    @Query( value = """
    SELECT *
                    FROM sample s
                    WHERE (s.is_delivered = FALSE OR s.is_delivered IS NULL)
                    AND s.status_reception = true
                      AND (s.due_date IS NULL OR s.due_date < current_date())
                    ORDER BY s.create_at ASC;
""", nativeQuery = true)
    List<SampleEntity> findAllExpiredAndNotDelivered(
            @Param("currentDate") LocalDate currentDate
    );


    Page<SampleEntity> findAllByIsDeliveredTrue(Pageable pageable);

    Page<SampleEntity> findAllByStatusReceptionFalse(Pageable pageable);

   @Query(
            value = """
        SELECT s.*
        FROM sample s
        INNER JOIN sample_product_analysis a
            ON a.sample_id = s.sample_id
        WHERE a.sample_product_analysis_id = :id
    """,
            nativeQuery = true
    )
   Optional<SampleEntity>  findSampleByAnalysisId(@Param("id") Long id);

}
