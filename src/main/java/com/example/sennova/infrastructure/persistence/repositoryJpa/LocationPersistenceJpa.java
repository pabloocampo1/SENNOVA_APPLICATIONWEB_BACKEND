package com.example.sennova.infrastructure.persistence.repositoryJpa;

import com.example.sennova.infrastructure.persistence.entities.LocationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LocationPersistenceJpa extends JpaRepository<LocationEntity, Long> {
    List<LocationEntity> findAllByLocationNameContainingIgnoreCase(String name);
    List<LocationEntity> findByLocationName(String name);
    boolean existsByLocationName(String name);
}
