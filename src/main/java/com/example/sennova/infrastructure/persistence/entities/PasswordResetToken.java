package com.example.sennova.infrastructure.persistence.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "password_reset_tokens", indexes = {
        @Index(
                name = "idx_password_reset_tokens_user_id",
                columnList = "user_id"
        )
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PasswordResetToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String token;

    private Long userId;

    // added this for validation with the user
    private String email;


    private LocalDateTime expireDate;



    public boolean isExpired(){
        return this.expireDate.isBefore(LocalDateTime.now());
    }
}

