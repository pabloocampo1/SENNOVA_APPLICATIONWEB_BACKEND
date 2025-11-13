package com.example.sennova.infrastructure.persistence.entities.inventoryReagentsEntities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Table(name = "reagent_media_files")
@Entity
@AllArgsConstructor
@Data
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
public class ReagentMediaFilesEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long reagentFileId;

    @Column(nullable = false)
    private String url;

    @Column(nullable = false)
    private String publicId;

    private String type;

    private String nameFile;

    @CreatedDate
    private LocalDateTime createAt;

    @LastModifiedDate
    private LocalDateTime updateAt;

    @ManyToOne()
    @JoinColumn(name = "reagent_id", referencedColumnName = "reagents_id")
    @JsonIgnore
    private ReagentsEntity reagentEntity;
}
