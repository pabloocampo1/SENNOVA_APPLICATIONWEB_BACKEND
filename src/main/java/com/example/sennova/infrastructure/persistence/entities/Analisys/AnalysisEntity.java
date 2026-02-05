package com.example.sennova.infrastructure.persistence.entities.Analisys;

import com.example.sennova.infrastructure.persistence.entities.UserEntity;
import com.example.sennova.infrastructure.persistence.entities.requestsEntities.SampleAnalysisEntity;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "analysis")
@Data
@EntityListeners(AuditingEntityListener.class)
public class AnalysisEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "analysis_id")
    private Long analysisId;

    @Column(nullable = false)
    private String analysisName;


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

    @ManyToMany
    @JoinTable(
            name = "analysis_responsible",
            joinColumns = @JoinColumn(name = "analysis_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<UserEntity> qualifiedUsers;

    @OneToMany(mappedBy = "product")
    private List<SampleAnalysisEntity> sampleProductAnalysisEntities;

    @ManyToMany
    @JoinTable(
            name = "matrix_analysis",
            joinColumns = @JoinColumn(name = "analysis_id"),
            inverseJoinColumns = @JoinColumn(name = "matrix_id")
    )
    public List<MatrixEntity> matrices;

    public void addMatrix(MatrixEntity matrix) {
        this.matrices.add(matrix);
        matrix.getAnalysis().add(this);
    }

}
