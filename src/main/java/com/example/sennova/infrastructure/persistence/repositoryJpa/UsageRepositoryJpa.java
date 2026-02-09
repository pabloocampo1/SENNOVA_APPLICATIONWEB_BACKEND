package com.example.sennova.infrastructure.persistence.repositoryJpa;

import com.example.sennova.infrastructure.persistence.entities.UsageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsageRepositoryJpa extends JpaRepository<UsageEntity, Long> {

    List<UsageEntity> findAllByUsageNameContainingIgnoreCase(String name);
    List<UsageEntity> findByUsageName(String name);
    boolean existsByUsageName(String name);
}
