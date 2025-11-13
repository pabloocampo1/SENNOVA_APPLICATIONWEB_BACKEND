package com.example.sennova.infrastructure.persistence.entities.analysisRequestsEntities;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "sample_product_document_result")
@Data
@EntityListeners(AuditingEntityListener.class)
public class SampleProductDocumentResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long SampleProductDocumentResultId;

    @Column(nullable = false)
    private String url;

    @ManyToOne
    @JoinColumn(name = "sample_product_analysis", referencedColumnName = "sample_product_analysis_id")
    private SampleAnalysisEntity sampleProductAnalysis;

}
