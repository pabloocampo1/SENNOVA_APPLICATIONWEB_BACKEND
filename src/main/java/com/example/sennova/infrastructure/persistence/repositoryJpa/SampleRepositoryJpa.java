package com.example.sennova.infrastructure.persistence.repositoryJpa;

import com.example.sennova.infrastructure.persistence.entities.analysisRequestsEntities.SampleEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface SampleRepositoryJpa extends JpaRepository<SampleEntity, Long> {
    @Query("SELECT s FROM SampleEntity s WHERE s.testRequest.testRequestId = :testRequestId")
    List<SampleEntity> findAllByTestRequest(@Param("testRequestId") Long testRequest);

    List<SampleEntity> findAllByStatusReceptionTrue();

    @Query( value = "SELECT s FROM SampleEntity s WHERE s.dueDate < :currentDate ORDER BY s.createAt ASC")
    List<SampleEntity> findAllByDueDateExpired(@Param("currentDate") LocalDate currentDate);

    Page<SampleEntity> findAllByIsDeliveredTrue(Pageable pageable);
}
