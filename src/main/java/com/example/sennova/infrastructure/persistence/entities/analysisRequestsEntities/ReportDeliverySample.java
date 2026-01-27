package com.example.sennova.infrastructure.persistence.entities.analysisRequestsEntities;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "report_delivery_sample",
        indexes = {
                @Index(
                        name = "idx_report_delivery_sample_sample_id",
                        columnList = "sample_id"
                ),
                @Index(
                        name = "idx_report_delivery_sample_request_code",
                        columnList = "request_code"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class ReportDeliverySample {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "delivery_id")
    private Long id;

    @Column(name = "sample_id", nullable = false)
    private Long sampleId;

    @Column(name = "sample_code", length = 100)
    private String sampleCode;

    private Long testRequestId;

    private String matrixSample;

    private int totalAnalysis;

    private String requestCode;

    private String responsibleName;

    private LocalDateTime sentAt;


    @Column(nullable = false, length = 20)
    private String status;


    @Column(length = 150)
    private String customerEmail;

    private String customerName;

    @CreatedDate
    private LocalDateTime createdAt;

   @LastModifiedDate
    private LocalDateTime updatedAt;


}
