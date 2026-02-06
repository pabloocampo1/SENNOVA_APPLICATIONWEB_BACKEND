package com.example.sennova.infrastructure.persistence.entities;

import com.example.sennova.infrastructure.persistence.entities.Analisys.AnalysisEntity;
import com.example.sennova.infrastructure.persistence.entities.requestsEntities.TestRequestEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Table(name = "users")
@Entity
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@NoArgsConstructor

public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private Long dni;

    private boolean available;

    @Column(nullable = false)
    private Long phoneNumber;

    @Column(nullable = false, unique = true)
    private String email;

    private String position;

    private String imageProfile;

    @Column(length = 1000)
    private String refreshToken;

    private boolean notifyEquipment;

    private boolean notifyReagents;

    private boolean notifyQuotes;

    private boolean notifyResults;

    private LocalDateTime lastSession;


    @CreatedDate
    private LocalDate createAt;

    @LastModifiedDate
    private LocalDate updateAt;

    // relationships
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id")
    private RoleEntity role;

    @ToString.Exclude
    @ManyToMany(mappedBy = "members")
    private List<TestRequestEntity> testRequestEntities;

    @ToString.Exclude
    @ManyToMany(mappedBy = "qualifiedUsers")
    @JsonIgnoreProperties("members")
    private List<AnalysisEntity> trainedAnalyses;


    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private VerificationEmail verificationEmails;

}
