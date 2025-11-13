package com.example.sennova.infrastructure.persistence.entities.analysisRequestsEntities;


import com.example.sennova.infrastructure.persistence.entities.CustomerEntity;
import com.example.sennova.infrastructure.persistence.entities.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "test_request")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor


@EntityListeners(AuditingEntityListener.class)
public class TestRequestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "test_request_id")
    private Long testRequestId;

    @Column(nullable = false, unique = true, length = 100, name = "request_code")
    private String requestCode;

   // private LocalDate testDate;

    @Column(nullable = true)
    private LocalDate approvalDate;

    private LocalDate discardDate;

    private LocalDate  dueDate;

    private LocalDate submissionDate;

    private Boolean isFinished;

    private String deliveryStatus;


    private double price;

    private Boolean isApproved;

    private String state;

    @CreatedDate
    private LocalDate createAt;

    @LastModifiedDate
    private LocalDate updateAt;

    @Column(length = 500)
    private String notes;

    // relationships

    @OneToMany(mappedBy = "testRequest")
    private List<SampleEntity> sampleEntityList;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id", referencedColumnName = "customer_id", nullable = false)
    private CustomerEntity customer;

    @ManyToMany
    @JoinTable(
            name = "test_user",
            joinColumns = @JoinColumn(name = "test_request_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<UserEntity> members;
}
