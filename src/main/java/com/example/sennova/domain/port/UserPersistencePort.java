package com.example.sennova.domain.port;

import com.example.sennova.domain.model.UserModel;
import com.example.sennova.infrastructure.persistence.entities.UserEntity;

import java.util.List;

public interface UserPersistencePort {
    UserModel findById(Long id);
    UserModel save(UserModel userModel);
    List<UserModel> findAll();
    List<UserModel> getAllByTestRequest(Long testRequestId);
    List<UserModel> findAllByAvailableTrue();
    UserModel update(UserModel userModel);
    void deleteUser(Long userId);
    List<UserModel> findByName(String name);
    List<UserModel> findByRole(Long roleId);
    List<UserModel> findByDni(Long dni);
    Boolean existByUserName(String username);
    Boolean existsById(Long id);
    UserModel findByUsername(String username);
    void saveRefreshToken(String refreshToken, String username);
    void deleteRefreshToken(String username);
    UserModel findByEmail(String email);
    UserEntity findEntityById(Long userId);
    boolean existByEmail(String email);
    List<UserModel> findAllById(List<Long> listOfId);
    List<UserModel> findAllUserByTestRequest(Long testRequestId);

}
