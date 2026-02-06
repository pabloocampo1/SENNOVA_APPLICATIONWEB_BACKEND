package com.example.sennova.infrastructure.persistence.entities.inventoryReagentsEntities;

import com.example.sennova.infrastructure.persistence.entities.UserEntity;
import com.example.sennova.infrastructure.persistence.entities.LocationEntity;
import com.example.sennova.infrastructure.persistence.entities.UsageEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.util.List;

@Table(name = "reagents")
@Entity
@Data
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"reagentsUsageRecordsList", "user", "usage", "location", "mediaFiles"})
public class ReagentsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reagents_id")
    private Long reagentsId;

    @Column(nullable = false)
    private String reagentName;

    private String brand;

    private String purity;

    @Column(nullable = false)
    private Integer units;

    @Column(nullable = false)
    private Double quantity;

    private String imageUrl;

    private String unitOfMeasure;

    private String batch;

    @Column(nullable = false)
    private LocalDate expirationDate;

    @Column(unique = true)
    private String senaInventoryTag;

    private String stateExpiration;

    private String state;

    private Boolean isPresent;

    @CreatedDate
    private LocalDate createAt;

    @LastModifiedDate
    private LocalDate updateAt;

    private String responsible;

    @ManyToOne
    @JoinColumn(name = "usage_id", referencedColumnName = "usage_id")
    private UsageEntity usage;

    @ManyToOne
    @JoinColumn(name = "location_id", referencedColumnName = "location_id")
    private LocationEntity location;

    @OneToMany(mappedBy = "reagentEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReagentMediaFilesEntity> mediaFiles;

    @OneToMany(mappedBy = "reagent", cascade = CascadeType.REMOVE)
    private List<ReagentsUsageRecords> reagentsUsageRecordsList;
}
