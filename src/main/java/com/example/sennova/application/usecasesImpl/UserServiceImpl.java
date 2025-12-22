package com.example.sennova.application.usecasesImpl;

import com.example.sennova.application.dto.UserDtos.*;
import com.example.sennova.application.mapper.UserMapper;
import com.example.sennova.application.usecases.UserUseCase;
import com.example.sennova.domain.model.RoleModel;
import com.example.sennova.domain.model.UserModel;
import com.example.sennova.domain.port.RolePersistencePort;
import com.example.sennova.domain.port.UserPersistencePort;
import com.example.sennova.infrastructure.persistence.entities.UserEntity;
import com.example.sennova.infrastructure.restTemplate.CloudinaryService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserServiceImpl implements UserUseCase {

    private final UserPersistencePort userPersistencePort;
    private final UserMapper userMapper;
    private final RolePersistencePort rolePersistencePort;
    private final PasswordEncoder passwordEncoder;
    private final CloudinaryService cloudinaryService;


    @Autowired
    public UserServiceImpl(UserPersistencePort userPersistencePort, UserMapper userMapper, RolePersistencePort rolePersistencePort, PasswordEncoder passwordEncoder, CloudinaryService cloudinaryService) {
        this.userPersistencePort = userPersistencePort;
        this.userMapper = userMapper;
        this.rolePersistencePort = rolePersistencePort;
        this.passwordEncoder = passwordEncoder;
        this.cloudinaryService = cloudinaryService;
    }


    @Override
    @Transactional
    public UserResponse save(UserSaveRequest userSaveRequest, MultipartFile multipartFile) {
        try {
            UserModel userModel = this.userMapper.toModel(userSaveRequest);

            // search y  validate if the role exist.
            RoleModel roleModel = this.rolePersistencePort.findByName(userSaveRequest.roleName());

            // add the role to the user
            userModel.setRole(roleModel);

            // if have one image save in cloudinary.
            if (multipartFile != null) {
                userModel.setImageProfile(this.cloudinaryService.uploadImage(multipartFile));
            }

            // active everything
            userModel.setNotifyResults(true);
            userModel.setAvailable(userSaveRequest.available());
            userModel.setNotifyEquipment(true);
            userModel.setNotifyResults(true);
            userModel.setCreateAt(LocalDate.now());
            userModel.setNotifyQuotes(true);
            userModel.setNotifyReagents(true);

            // encrip one password by default (the user can change after)
            userModel.setPassword(passwordEncoder.encode(userModel.getDni().toString()));
            userModel.setUsername(userModel.getDni().toString());

            // save the user
            UserModel userSaved = this.userPersistencePort.save(userModel);

            // return the response
            return this.userMapper.toResponse(userSaved);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("Duplicate entry");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public UserResponse saveModel(UserModel userModel) {
        UserModel userModel1 = this.userPersistencePort.save(userModel);
        return this.userMapper.toResponse(userModel1);
    }

    @Override
    public List<UserResponse> findAll() {
        return this.userMapper.toResponse(this.userPersistencePort.findAll());
    }

    @Override
    public List<UserModel> findAllModels() {
        return this.userPersistencePort.findAll();
    }

    @Override
    public List<UserResponseMembersAssigned> getAllByTestRequest(Long testRequestId) {
        return this.userPersistencePort.getAllByTestRequest(testRequestId).stream().map(userModel -> {
            return new UserResponseMembersAssigned(
                    userModel.getUserId(),
                    userModel.getName(),
                    userModel.getAvailable(),
                    userModel.getImageProfile(),
                    userModel.getEmail()
            );
        }).toList();
    }

    @Override
    public List<UserResponse> getAllAvailable() {
        List<UserModel> allAvailable = this.userPersistencePort.findAllByAvailableTrue();
        return allAvailable.stream().map(this.userMapper::toResponse).toList();
    }

    @Override
    public UserResponse findById(@Valid Long id) {
        UserModel userModel = this.userPersistencePort.findById(id);
        return this.userMapper.toResponse(userModel);
    }


    @Override
    @Transactional
    public UserResponse update(@Valid Long userId, @Valid UserUpdateDto userUpdateDto, MultipartFile imageFile) {

        if (!this.userPersistencePort.existsById(userUpdateDto.userId())) {
            throw new UsernameNotFoundException("El usuario " + userUpdateDto.name() + " no existe.");
        }


        UserModel currentUserInfo = this.userPersistencePort.findById(userId);
        UserModel user = new UserModel();

        RoleModel roleModel = this.rolePersistencePort.findByName(userUpdateDto.roleName());

        // add the role to the user
        user.setRole(roleModel);

        user.setUserId(userUpdateDto.userId());
        user.setName(userUpdateDto.name());
        user.setDni(userUpdateDto.dni());
        user.setAvailable(userUpdateDto.available());
        user.setPhoneNumber(userUpdateDto.phoneNumber());
        user.setEmail(userUpdateDto.email());
        user.setPosition(userUpdateDto.position());


        if (imageFile != null) {
            user.setImageProfile(this.cloudinaryService.uploadImage(imageFile));
        } else {
            user.setImageProfile(currentUserInfo.getImageProfile());
        }


        return this.userMapper.toResponse(this.userPersistencePort.update(user));

    }

    @Override
    public void deleteUser(@Valid Long userId) {
        if (!this.userPersistencePort.existsById(userId)) {
            throw new UsernameNotFoundException("No se encontro ese usuario para eliminar");
        }

        this.userPersistencePort.deleteUser(userId);
    }

    @Override
    public List<UserResponse> findByName(String name) {
        List<UserResponse> userResponseList = this.userPersistencePort.findByName(name)
                .stream()
                .map(this.userMapper::toResponse)
                .toList();

        return userResponseList;
    }

    @Override
    public UserModel findByUsername(@Valid String username) {
        return this.userPersistencePort.findByUsername(username);
    }

    @Override
    public List<UserResponse> findByRole(@Valid Long roleId) {
        List<UserResponse> userResponseList = this.userPersistencePort.findByRole(roleId)
                .stream()
                .map(this.userMapper::toResponse)
                .toList();

        return userResponseList;
    }

    @Override
    public List<UserResponse> findByDni(Long dni) {
        List<UserResponse> userResponseList = this.userPersistencePort.findByDni(dni)
                .stream()
                .map(this.userMapper::toResponse)
                .toList();

        return userResponseList;
    }

    @Override
    public Boolean existByUsername(String username) {
        return this.userPersistencePort.existByUserName(username);
    }

    @Override
    public void saveRefreshToken(String refreshToken, String username) {
        this.userPersistencePort.saveRefreshToken(refreshToken, username);
    }

    @Override
    public void deleteRefreshToken(@Valid String username) {
        this.userPersistencePort.deleteRefreshToken(username);
    }

    @Override
    public UserModel getByEmail(String email) {
        return this.userPersistencePort.findByEmail(email);
    }

    @Override
    public UserPreferenceResponse changePreference(@Valid UserPreferencesRequestDto userPreferencesRequestDto, @Valid String username) {
        UserModel userModel = this.userPersistencePort.findByUsername(username);

        userModel.setNotifyEquipment(userPreferencesRequestDto.inventoryEquipment());
        userModel.setNotifyReagents(userPreferencesRequestDto.inventoryReagents());
        userModel.setNotifyQuotes(userPreferencesRequestDto.quotations());
        userModel.setNotifyResults(userPreferencesRequestDto.results());
        UserModel userUpdate = this.userPersistencePort.save(userModel);
        UserPreferenceResponse userPreferenceResponse = new UserPreferenceResponse(userModel.isNotifyEquipment(), userModel.isNotifyReagents(), userModel.isNotifyQuotes(), userModel.isNotifyResults());
        return userPreferenceResponse;
    }

    @Override
    public String changeEmail(String currentEmail, String newEmail) {
        UserModel userModel = this.userPersistencePort.findByEmail(currentEmail);

        if (this.userPersistencePort.existByEmail(newEmail)) {
            throw new IllegalArgumentException("El nuevo email no esta disponible");
        }

        userModel.setEmail(newEmail);

        UserModel userUpdated = this.userPersistencePort.update(userModel);

        return userUpdated.getEmail();
    }

    @Override
    public boolean existByEmail(@Valid String email) {
        return this.userPersistencePort.existByEmail(email);
    }

    @Override
    public UserEntity getEntity(Long userId) {
        return this.userPersistencePort.findEntityById(userId);
    }

    @Override
    public void saveTheLastSession(LocalDateTime date, Long userId) {
        UserModel userModel = this.userPersistencePort.findById(userId);
        userModel.setLastSession(date);
        this.userPersistencePort.save(userModel);
    }

    @Override
    public Boolean deactiveAccount(String username) {
        UserModel userModel = this.userPersistencePort.findByUsername(username);
        userModel.setAvailable(false);
        this.userPersistencePort.save(userModel);

        return true;
    }

    @Override
    public Boolean activeAccount(String username) {
        UserModel userModel = this.userPersistencePort.findByUsername(username);
        userModel.setAvailable(true);
        this.userPersistencePort.save(userModel);

        return true;
    }

    @Override
    public Boolean existById(Long userUd) {
        return this.userPersistencePort.existsById(userUd);
    }

    @Override
    public List<UserModel> findAllById(List<Long> listOfId) {
        return this.userPersistencePort.findAllById(listOfId);
    }

    @Override
    public List<UserResponse> usersAssignedTestRequest(Long testRequestId) {
        List<UserModel> users = this.userPersistencePort.findAllUserByTestRequest(testRequestId);
        return users.stream().map(this.userMapper::toResponse).toList();
    }
}
