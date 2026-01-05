package com.example.sennova.infrastructure.persistence.entities.analysisRequestsEntities;

import jakarta.persistence.*;
import lombok.Data;
import net.minidev.json.annotate.JsonIgnore;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "sample")
@Data
@EntityListeners(AuditingEntityListener.class)
public class SampleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true, name = "sample_id")
    private Long sampleId;

    @Column(nullable = false)
    private String matrix;

    @Lob
    @Column(columnDefinition = "MEDIUMTEXT")
    private String description;

    @Column(nullable = false, unique = true)
    private String sampleCode;

    private LocalDate sampleEntryDate;

    private LocalDate sampleReceptionDate;

    private String identificationSample;

    private double gross_weight;

    private double temperature;

    private String packageDescription;

    private String storageConditions;

    private Boolean isDelivered;

    private LocalDate deliveryDate;

    private String observations;

    private String samplingLocation;

    private Boolean statusReception;

    private Integer totalAnalysis;

    private String sampleImage;

    // This due date is generated when the laboratory registers the reception.
    // It's slightly different from the test request due date, which is generated
    // when all samples have been received.
    private LocalDate dueDate;


    @CreatedDate
    private LocalDate createAt;

    @LastModifiedDate
    private LocalDate updateAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "test_request_id", referencedColumnName = "test_request_id", nullable = false)
    private TestRequestEntity testRequest;

    @OneToMany(mappedBy = "sample", fetch = FetchType.EAGER)
    @JsonIgnore
    private List<SampleAnalysisEntity> analysisEntities;

}
