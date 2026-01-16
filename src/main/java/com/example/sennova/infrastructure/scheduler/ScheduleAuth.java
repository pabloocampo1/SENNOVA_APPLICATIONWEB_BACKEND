package com.example.sennova.infrastructure.scheduler;

import com.example.sennova.infrastructure.persistence.repositoryJpa.PasswordResetTokenJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class ScheduleAuth {

    private final PasswordResetTokenJpaRepository passwordResetTokenJpaRepository;

    @Autowired
    public ScheduleAuth(PasswordResetTokenJpaRepository passwordResetTokenJpaRepository) {
        this.passwordResetTokenJpaRepository = passwordResetTokenJpaRepository;
    }

    @Scheduled(cron = "0 0 0 * * ?", zone = "America/Bogota")
    @Transactional
    public void removeTokensExpired() {
         this.passwordResetTokenJpaRepository.deleteAllByExpireDateBefore(LocalDateTime.now());
    }

}
