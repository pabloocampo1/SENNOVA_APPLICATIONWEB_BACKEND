package com.example.sennova.infrastructure.persistence.entities.inventoryReagentsEntities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.List;

@Table(name = "usage_reagents")
@Entity
@Data
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
public class UsageReagentsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "usage_reagent_id")
    private Long usageReagentId;

    @Column(nullable = false)
    private String usage_name;

}
