package com.example.sennova.infrastructure.persistence.entities.analysisRequestsEntities;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "sample_product_analysis")
@Data
@EntityListeners(AuditingEntityListener.class)
public class SampleAnalysisEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sample_product_analysis_id")
    private Long SampleProductAnalysisId;

    // relationship n:n
    @ManyToOne
    @JoinColumn(name = "sample_id", referencedColumnName = "sample_id")
    private SampleEntity sample;

    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "product_id")
    private ProductEntity product;

    private String resultFinal;

    private LocalDate resultDate;

    @Column(length = 400)
    private String notes;

    private boolean stateResult;

    @CreatedDate
    private LocalDate createAt;

    @LastModifiedDate
    private LocalDate updateAt;

    @OneToMany(mappedBy = "sampleProductAnalysis")
    private List<SampleProductDocumentResult> sampleProductDocumentResult;




}
