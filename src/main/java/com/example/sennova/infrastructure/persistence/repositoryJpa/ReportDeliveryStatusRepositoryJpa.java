package com.example.sennova.infrastructure.persistence.repositoryJpa;

import com.example.sennova.infrastructure.persistence.entities.analysisRequestsEntities.ReportDeliverySample;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportDeliveryStatusRepositoryJpa extends JpaRepository<ReportDeliverySample, Long> {
}
