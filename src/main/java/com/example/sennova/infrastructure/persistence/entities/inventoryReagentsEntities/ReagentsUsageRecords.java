package com.example.sennova.infrastructure.persistence.entities.inventoryReagentsEntities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

@Table(name = "reagent_usage_records")
@Entity
@Data
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@NoArgsConstructor
public class ReagentsUsageRecords {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reagents_usage_records_id")
    private Long reagentUsageRecordsId;

    @Column(nullable = false)
    private String usedBy;

    @Column(nullable = false)
    private Double quantity_used;

    private Double previousQuantity;

    @Column(length = 400)
    private String notes;

    @CreatedDate
    private LocalDate createAt;

    @LastModifiedDate
    private LocalDate updateAt;

    @ManyToOne
    @JoinColumn(name = "reagents_id", referencedColumnName = "reagents_id")
    @JsonIgnore
    private ReagentsEntity reagent;
}
