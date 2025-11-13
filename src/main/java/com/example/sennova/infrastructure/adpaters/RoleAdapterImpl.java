package com.example.sennova.infrastructure.adpaters;

import com.example.sennova.domain.model.RoleModel;
import com.example.sennova.domain.port.RolePersistencePort;
import com.example.sennova.infrastructure.mapperDbo.RoleMapperDbo;
import com.example.sennova.infrastructure.persistence.entities.RoleEntity;
import com.example.sennova.infrastructure.persistence.repositoryJpa.RoleRepositoryJpa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RoleAdapterImpl implements RolePersistencePort {

    private final RoleRepositoryJpa roleRepositoryJpa;
    private final RoleMapperDbo roleMapperDbo;

    @Autowired
    public RoleAdapterImpl(RoleRepositoryJpa roleRepositoryJpa, RoleMapperDbo roleMapperDbo) {
        this.roleRepositoryJpa = roleRepositoryJpa;
        this.roleMapperDbo = roleMapperDbo;
    }

    @Override
    public RoleModel findByName(String nameRole) {
        RoleEntity roleEntity = this.roleRepositoryJpa.findByNameRole(nameRole)
                .orElseThrow(() -> new IllegalArgumentException("El rol de " + nameRole + "no existe."));
        return this.roleMapperDbo.toModel(roleEntity);
    }

    @Override
    public List<RoleModel> getAll() {
        List<RoleEntity> entities = (List<RoleEntity>) this.roleRepositoryJpa.findAll();
        return entities.stream().map(this.roleMapperDbo::toModel).toList();
    }
}
