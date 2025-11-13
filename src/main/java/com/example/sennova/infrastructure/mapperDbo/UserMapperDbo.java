package com.example.sennova.infrastructure.mapperDbo;

import com.example.sennova.domain.model.UserModel;
import com.example.sennova.infrastructure.persistence.entities.UserEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {RoleMapperDbo.class})
public interface UserMapperDbo {
    UserModel toModel(UserEntity userEntity);
    UserEntity toEntity(UserModel userModel);
    List<UserModel> toModel(Iterable<UserEntity> userEntityIterable);
}
