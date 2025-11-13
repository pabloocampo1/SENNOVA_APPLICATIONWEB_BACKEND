package com.example.sennova.infrastructure.persistence.entities.inventoryEquipmentEntities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;


@Table(name = "maintenance_record_equipment")
@Entity
@Data
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@NoArgsConstructor
public class MaintenanceRecordsEquipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "maintenance_record_equipment_id")
    private Long maintenanceEquipmentId;

    @Column(nullable = false)
    private String performedBy;

    private String maintenanceType;

    @Column(length = 400)
    private String notes;

    @Column(nullable = false)
    private LocalDate dateMaintenance;

    @CreatedDate
    private LocalDate createAt;

    @LastModifiedDate
    private LocalDate updateAt;

    @ManyToOne
    @JoinColumn(name = "equipment_id", referencedColumnName = "equipment_id")
    private EquipmentEntity equipment;

}
