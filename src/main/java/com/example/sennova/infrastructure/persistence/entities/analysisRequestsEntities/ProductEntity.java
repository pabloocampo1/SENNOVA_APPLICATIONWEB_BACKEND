package com.example.sennova.infrastructure.persistence.entities.analysisRequestsEntities;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "product")
@Data
@EntityListeners(AuditingEntityListener.class)
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long productId;

    @Column(nullable = false)
    private String analysis;

    @Column(nullable = false)
    private String matrix;

    @Column(nullable = false)
    private String method;

    @Column(nullable = false)
    private String equipment;

    @Column(nullable = false)
    private String units;

    @Column(nullable = false)
    private double price;

    @CreatedDate
    private LocalDate createAt;

    @LastModifiedDate
    private LocalDate updateAt;

    private Boolean available;

    @Column(length = 500)
    private String notes;

    @OneToMany(mappedBy = "product")
    private List<SampleAnalysisEntity> sampleProductAnalysisEntities;

}
