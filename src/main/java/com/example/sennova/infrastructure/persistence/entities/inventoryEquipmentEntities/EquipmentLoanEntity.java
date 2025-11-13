package com.example.sennova.infrastructure.persistence.entities.inventoryEquipmentEntities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Table(name = "equipment_loan")
@Entity
@Data
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor   // 👈 genera el constructor vacío requerido por JPA
@AllArgsConstructor  // 👈 genera el constructor con todos los args
@Builder
public class EquipmentLoanEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "equipment_loan_id")
    private Long equipmentLoanId;

    @Column(nullable = false)
    private String nameLoan;

    @Column(length = 400)
    private String notes;

    private LocalDate loanDate;

    @CreatedDate
    private LocalDate createAt;

    @LastModifiedDate
    private LocalDate updateAt;

    @Column(nullable = false)
    private String type;

    @ManyToOne()
    @JoinColumn(name = "equipment_id", referencedColumnName = "equipment_id", nullable = false)
    @JsonIgnore
    private EquipmentEntity equipment;

}
