package com.example.sennova.application.usecases;

import com.example.sennova.application.dto.UserDtos.*;
import com.example.sennova.domain.model.UserModel;
import com.example.sennova.infrastructure.persistence.entities.UserEntity;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

public interface UserUseCase {
    UserResponse save(UserSaveRequest userSaveRequest, MultipartFile multipartFile);
    UserResponse saveModel(UserModel userModel);
    List<UserResponse> findAll();
    List<UserModel> findAllModels();
    List<UserResponse> getAllByTestRequest(Long testRequestId);
    List<UserResponse> getAllAvailable();
    UserResponse findById(Long id);
    UserResponse update(Long userId, UserUpdateDto userUpdateDto, MultipartFile imageFile);
    void deleteUser(Long userId);
    List<UserResponse> findByName(String name);
    UserModel findByUsername(String username);
    List<UserResponse> findByRole(Long roleId);
    List<UserResponse> findByDni(Long dni);
    Boolean existByUsername(String username);
    void saveRefreshToken(String refreshToken, String username);
    void deleteRefreshToken(String username);
    UserModel getByEmail(String email);
    UserPreferenceResponse changePreference(UserPreferencesRequestDto userPreferencesRequestDto, String username);
    String changeEmail(String currentEmail, String newEmail);
    boolean existByEmail(String email);
    UserEntity getEntity(Long userId);
    void saveTheLastSession(LocalDateTime date, Long userId);
    Boolean deactiveAccount(String username);
    Boolean activeAccount(String username);
    List<UserModel> findAllById(List<Long> listOfId);
    List<UserResponse> usersAssignedTestRequest(Long testRequestId);
}
