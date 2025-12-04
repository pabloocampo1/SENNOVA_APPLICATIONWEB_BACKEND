package com.example.sennova.infrastructure.persistence.repositoryJpa;

import com.example.sennova.infrastructure.persistence.entities.analysisRequestsEntities.SampleProductDocumentResult;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnalysisDocumentRepositoryJpa extends JpaRepository<SampleProductDocumentResult, Long> {
}
