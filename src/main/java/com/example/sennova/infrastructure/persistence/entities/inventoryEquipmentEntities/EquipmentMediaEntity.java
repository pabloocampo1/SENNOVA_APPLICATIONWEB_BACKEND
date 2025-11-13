package com.example.sennova.infrastructure.persistence.entities.inventoryEquipmentEntities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "equipment_media")
@Data
@EntityListeners(AuditingEntityListener.class)
public class EquipmentMediaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long equipmentMediaId;

    @Column(nullable = false)
    private String url;

    @Column(nullable = false)
    private String publicId;

    private String type;

    private String nameFile;

    @ManyToOne
    @JoinColumn(name = "equipment_id", referencedColumnName = "equipment_id", nullable = false)
    @JsonIgnore
    private EquipmentEntity equipment;

    @CreatedDate
    private LocalDate createAt;

    @LastModifiedDate
    private LocalDate updateAt;

    public EquipmentMediaEntity(String url, String publicId, String type, String nameFile, EquipmentEntity equipment) {
        this.url = url;
        this.publicId = publicId;
        this.type = type;
        this.nameFile = nameFile;
        this.equipment = equipment;
    }
}
