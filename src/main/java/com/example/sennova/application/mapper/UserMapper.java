package com.example.sennova.application.mapper;

import com.example.sennova.application.dto.UserDtos.UserListResponse;
import com.example.sennova.application.dto.UserDtos.UserResponse;
import com.example.sennova.application.dto.UserDtos.UserSaveRequest;
import com.example.sennova.domain.model.UserModel;
import com.example.sennova.infrastructure.persistence.entities.UserEntity;
import org.hibernate.annotations.Comments;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "roleName", source = "role.nameRole")
    UserResponse toResponse(UserModel userModel);
    @Mapping(target = "role", source = "role.nameRole")
    UserListResponse toUserListResponse(UserModel userModel);

    UserModel toModel(UserSaveRequest userSaveRequest);
    UserModel toModel(UserResponse userResponse);
    List<UserResponse> toResponse(Iterable<UserModel> userModelIterable);

    List<UserListResponse> toResponseUserList(Iterable<UserModel> userModelIterable);

}
