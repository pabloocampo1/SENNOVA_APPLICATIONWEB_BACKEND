package com.example.sennova.domain.model;

import com.example.sennova.infrastructure.persistence.entities.inventoryEquipmentEntities.EquipmentEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDate;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MaintenanceRecordEquipmentModel {

    private Long maintenanceEquipmentId;

    @NotBlank(message = "Debe contener el nombre de la persona")
    private String performedBy;

    @NotBlank(message = "Debe contener un tipo de mantenimiento")
    private String maintenanceType;

    @Size(max = 400, message = "La nota no debe de contener más de 400 caracteres")
    private String notes;

    @NotNull(message = "Debes de agregar la fecha para el registro")
    private LocalDate dateMaintenance;

    @NotNull(message = "El registro debe de contener el equipo, intentalo de nuevo")
    @JsonIgnore
    private EquipmentModel equipment;
}
