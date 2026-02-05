package com.example.sennova.infrastructure.persistence.entities.Analisys;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.util.List;

@Table(name = "matrix_entity")
@Entity
@Data
@EntityListeners(AuditingEntityListener.class)
public class MatrixEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long matrixId;


    @Column(nullable = false, name = "matrix_name")
    private String matrixName;

    private Boolean available;

    @CreatedDate
    public LocalDate createdAt;

    @LastModifiedDate
    public LocalDate updatedAt;

    @ManyToMany(mappedBy = "matrices")
    private List<AnalysisEntity> analysis;

}
