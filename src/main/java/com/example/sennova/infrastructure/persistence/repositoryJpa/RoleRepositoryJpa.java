package com.example.sennova.infrastructure.persistence.repositoryJpa;

import com.example.sennova.infrastructure.persistence.entities.RoleEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RoleRepositoryJpa extends CrudRepository<RoleEntity, Long> {

    Optional<RoleEntity> findByNameRole(String nameRole);
}
