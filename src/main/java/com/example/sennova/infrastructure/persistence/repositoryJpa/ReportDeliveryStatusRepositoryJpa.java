package com.example.sennova.infrastructure.persistence.repositoryJpa;

import com.example.sennova.infrastructure.persistence.entities.requestsEntities.ReportDeliverySample;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportDeliveryStatusRepositoryJpa extends JpaRepository<ReportDeliverySample, Long> {

    List<ReportDeliverySample> findAllByTestRequestId(Long testRequestId);
    List<ReportDeliverySample> findTop10ByOrderByCreatedAtDesc();

}
