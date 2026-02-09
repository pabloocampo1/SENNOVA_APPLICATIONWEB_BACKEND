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


    private String analysisName;



    private String method;


    private String equipment;


    private String units;


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


    public AnalysisEntity(
            String analysisName,
            Boolean available,
            String method,
            String equipment,
            String units,
            double price,
            String notes
    ) {
        this.analysisName = analysisName;
        this.available = available;
        this.method = method;
        this.equipment = equipment;
        this.units = units;
        this.price = price;
        this.notes = notes;
    }

    public AnalysisEntity() {
    }

    public void addMatrix(MatrixEntity matrix) {
        this.matrices.add(matrix);
        matrix.getAnalysis().add(this);
    }

}
