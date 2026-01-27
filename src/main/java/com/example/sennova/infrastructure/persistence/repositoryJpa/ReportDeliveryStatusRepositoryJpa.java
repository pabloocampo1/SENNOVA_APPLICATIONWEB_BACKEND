package com.example.sennova.infrastructure.persistence.repositoryJpa;

import com.example.sennova.infrastructure.persistence.entities.analysisRequestsEntities.ReportDeliverySample;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportDeliveryStatusRepositoryJpa extends JpaRepository<ReportDeliverySample, Long> {

    List<ReportDeliverySample> findAllByRequestCode(String requestCode);
    List<ReportDeliverySample> findTop10ByOrderByCreatedAtDesc();

}
