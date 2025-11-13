package com.example.sennova.infrastructure.persistence.repositoryJpa;

import com.example.sennova.infrastructure.persistence.entities.UserEntity;
import com.example.sennova.infrastructure.persistence.entities.VerificationEmail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface VerificationEmailRepositoryJpa  extends JpaRepository<VerificationEmail, Long> {

    boolean existsByCode(Integer token);

    boolean existsByUser(UserEntity user);
    Optional<VerificationEmail> findByUser(UserEntity user);

    VerificationEmail findByCode(Integer code);

    @Modifying
    @Query("DELETE FROM VerificationEmail v WHERE v.user.userId = :userId")
    void deleteByUser(@Param("userId") Long userId);

}
