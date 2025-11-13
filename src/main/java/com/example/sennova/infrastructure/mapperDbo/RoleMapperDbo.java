package com.example.sennova.infrastructure.mapperDbo;

import com.example.sennova.domain.model.RoleModel;
import com.example.sennova.infrastructure.persistence.entities.RoleEntity;
import org.mapstruct.Mapper;

@Mapper( componentModel = "spring")
public interface RoleMapperDbo {


    RoleModel toModel(RoleEntity roleEntity);
    RoleEntity toEntity(RoleModel roleModel);
}
