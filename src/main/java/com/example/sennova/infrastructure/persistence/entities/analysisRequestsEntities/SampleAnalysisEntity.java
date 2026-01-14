package com.example.sennova.infrastructure.persistence.entities.analysisRequestsEntities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

    private String unit;

    private String passStatus;

    private String accreditationStatus;

    private String standards;

    private String resultGeneratedBy;

    private String code;

    @CreatedDate
    private LocalDateTime createAt;

    @LastModifiedDate
    private LocalDateTime updateAt;

    @OneToMany(mappedBy = "sampleProductAnalysis",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    @Fetch(FetchMode.SUBSELECT)
    @JsonManagedReference
    private List<SampleProductDocumentResult> sampleProductDocumentResult;


}
