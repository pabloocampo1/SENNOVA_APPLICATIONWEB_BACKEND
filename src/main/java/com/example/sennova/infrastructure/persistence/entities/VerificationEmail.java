package com.example.sennova.infrastructure.persistence.entities;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "verification_emails")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VerificationEmail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 5)
    private Integer code;

    @Column(nullable = false)
    private LocalDateTime expiryDate;

    @Column(nullable = false, unique = true)
    private String newEmail;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    public VerificationEmail(Integer code, LocalDateTime expiryDate, String newEmail, UserEntity user) {
        this.code = code;
        this.expiryDate = expiryDate;
        this.newEmail = newEmail;
        this.user = user;
    }
}

