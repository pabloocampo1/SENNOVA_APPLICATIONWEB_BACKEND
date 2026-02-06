package com.example.sennova.infrastructure.persistence.entities.inventoryEquipmentEntities;


import com.example.sennova.infrastructure.persistence.entities.LocationEntity;
import com.example.sennova.infrastructure.persistence.entities.UsageEntity;
import com.example.sennova.infrastructure.persistence.entities.UserEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;



/**
 * ⚠️ TEMPORARY NOTE:
 * Certain validation constraints (e.g., not-null, unique fields) were relaxed
 * to allow bulk equipment import from Excel files.
 *
 * If this data-loading process is complete, re-enable the validation logic
 * in both the entity and service layers to maintain database integrity.
 *
 * Context: The import process bypasses some standard validations to prevent
 * constraint violations when inserting legacy or incomplete records.
 */


@Entity
@Table(name = "equipment")
@Data
@EntityListeners(AuditingEntityListener.class)
public class EquipmentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "equipment_id")
    private Long equipmentId;

    @Column(unique = true, nullable = false, name = "internal_code")
    private String internalCode;

    @Column(nullable = false)
    private String equipmentName;

    private String brand;

    private String model;

    @Column(unique = true)
    private String serialNumber;

    private Boolean markReport;

    private LocalDate acquisitionDate;

    private String amperage;

    @Column(unique = true)
    private String senaInventoryTag;

    private String voltage;

    private double equipmentCost;

    private String imageUrl;

    @Column(nullable = false)
    private LocalDate maintenanceDate;

    private String state;


    private Boolean available;

    @CreatedDate
    private LocalDateTime createAt;

    @LastModifiedDate
    private LocalDateTime updateAt;

    @Lob
    @Column(columnDefinition = "MEDIUMTEXT")
    private String description;

    // fk with other entities
    private String responsible;

    @ManyToOne
    @JoinColumn(name = "location_id", referencedColumnName = "location_id", nullable = true)
    private LocationEntity location;


    @ManyToOne
    @JoinColumn(name = "usage_id", referencedColumnName = "usage_id", nullable = true)
    private UsageEntity usage;

    // instance of reference for relationship
    @OneToMany(mappedBy = "equipment", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<EquipmentLoanEntity> loanEntities;

    @OneToMany(mappedBy = "equipment", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<MaintenanceRecordsEquipment> maintenanceRecordsEquipments;

    @OneToMany(mappedBy = "equipment", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<EquipmentMediaEntity> equipmentMediaEntities;


}






