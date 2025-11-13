package com.example.sennova.infrastructure.persistence.entities;

import com.example.sennova.infrastructure.persistence.entities.analysisRequestsEntities.TestRequestEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "customer")
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@NoArgsConstructor
public class CustomerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private Long customerId;

    @Column(nullable = false, length = 200)
    private String customerName;

    @Column(nullable = true, length = 200, unique = false)
    private String dni;

    @Column(nullable = false, unique = false, length = 100)
    private String email;

    @Column(nullable = false, unique = false)
    private Long phoneNumber;

    @Column(length = 200)
    private String address;

    @Column(length = 100)
    private String city;

    @CreatedDate
    private LocalDate createAt;

    @LastModifiedDate
    private LocalDate updateAt;

    // relationships
    @OneToMany(mappedBy = "customer")
    private List<TestRequestEntity> testRequestEntityList;
}
