package com.example.sennova.infrastructure.adpaters;

import com.example.sennova.application.dto.UserDtos.UserCompetenceDTO;
import com.example.sennova.application.mapper.UserMapper;
import com.example.sennova.domain.model.UserModel;
import com.example.sennova.domain.model.testRequest.TestRequestModel;
import com.example.sennova.domain.port.UserPersistencePort;
import com.example.sennova.infrastructure.mapperDbo.TestRequestMapperDbo;
import com.example.sennova.infrastructure.mapperDbo.UserMapperDbo;
import com.example.sennova.infrastructure.persistence.entities.RoleEntity;
import com.example.sennova.infrastructure.persistence.entities.UserEntity;
import com.example.sennova.infrastructure.persistence.entities.requestsEntities.TestRequestEntity;
import com.example.sennova.infrastructure.persistence.repositoryJpa.RoleRepositoryJpa;
import com.example.sennova.infrastructure.persistence.repositoryJpa.UserRepositoryJpa;
import com.example.sennova.web.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserAdapterImpl implements UserPersistencePort {

    private final UserRepositoryJpa userRepositoryJpa;
    private final UserMapperDbo userMapperDbo;
    private final RoleRepositoryJpa roleRepositoryJpa;
    private final TestRequestMapperDbo testRequestMapperDbo;

    @Autowired
    public UserAdapterImpl(UserRepositoryJpa userRepositoryJpa, UserMapperDbo userMapperDbo, RoleRepositoryJpa roleRepositoryJpa, TestRequestMapperDbo testRequestMapperDbo) {
        this.userRepositoryJpa = userRepositoryJpa;
        this.userMapperDbo = userMapperDbo;
        this.roleRepositoryJpa = roleRepositoryJpa;
        this.testRequestMapperDbo = testRequestMapperDbo;
    }

    @Override
    public UserModel findById(Long id) {
        UserEntity userEntity = this.userRepositoryJpa.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro el usuario con id : " + id));
        return this.userMapperDbo.toModel(userEntity);
    }

    @Override
    public UserModel save(UserModel userModel) {
        UserEntity userEntity = this.userMapperDbo.toEntity(userModel);
        RoleEntity role = this.roleRepositoryJpa.findByNameRole(userModel.getRole().getNameRole())
                .orElseThrow(() -> new IllegalArgumentException("El rol de " + userModel.getRole().getNameRole() + "no existe."));
        userEntity.setRole(role);
        UserEntity userSaved = this.userRepositoryJpa.save(userEntity);
        System.out.println("user saved: " + userSaved);
        return this.userMapperDbo.toModel(userSaved);
    }

    @Override
    public List<UserModel> findAll() {
        return this.userMapperDbo.toModel(this.userRepositoryJpa.findAll());
    }

    @Override
    public List<UserModel> getAllByTestRequest(Long testRequestId) {
        List<UserEntity> teamUsers = this.userRepositoryJpa.findAllByTestRequest(testRequestId);
        return teamUsers.stream().map(this.userMapperDbo::toModel).toList();
    }

    @Override
    public List<UserModel> findAllByAvailableTrue() {
        List<UserEntity> users = this.userRepositoryJpa.findAllByAvailableTrue();
        return users.stream().map(this.userMapperDbo::toModel).toList();
    }

    @Override
    public UserModel update(UserModel userModel) {
        UserEntity userWithDataImportant = this.userRepositoryJpa.findById(userModel.getUserId()).orElseThrow();
        UserEntity userEntity = this.userMapperDbo.toEntity(userModel);

        userEntity.setPassword(userWithDataImportant.getPassword());
        userEntity.setUsername(userWithDataImportant.getUsername());
        UserEntity userUpdated = this.userRepositoryJpa.save(userEntity);

        return  this.userMapperDbo.toModel(userUpdated);
    }

    @Override
    public void deleteUser(Long userId) {
        this.userRepositoryJpa.deleteById(userId);
    }

    @Override
    public List<UserModel> findByName(String name) {
        List<UserEntity> userEntityList = this.userRepositoryJpa.findAllByNameContainingIgnoreCase(name);

        return userEntityList.stream().map(this.userMapperDbo::toModel).toList();
    }

    @Override
    public List<UserModel> findByRole(Long roleId) {
        List<UserEntity> userEntityList = this.userRepositoryJpa.findAllByRole(roleId);
        return userEntityList.stream().map(this.userMapperDbo::toModel).toList();
    }

    @Override
    public List<UserModel> findByDni(Long dni) {
        List<UserEntity> userEntityList = this.userRepositoryJpa.findAllByDni(dni);
        return userEntityList.stream().map(this.userMapperDbo::toModel).toList();
    }

    @Override
    public List<UserModel> findAllUserByTestRequest(Long testRequestId) {
        List<UserEntity> users = this.userRepositoryJpa.findAllUserByTestRequestId(testRequestId);
        return users.stream().map(this.userMapperDbo::toModel).toList();
    }

    @Override
    public List<String> findUnauthorizedAnalyses(Long requestId, Long userId) {
        return this.userRepositoryJpa.findUnauthorizedAnalyses(requestId, userId);
    }

    @Override
    public Boolean existByUserName(String username) {
        return this.userRepositoryJpa.existsByUsername(username);
    }

    @Override
    public Boolean existsById(Long id) {
        return this.userRepositoryJpa.existsById(id);
    }

    @Override
    public UserModel findByUsername(String username) {

        UserEntity user = this.userRepositoryJpa.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        return this.userMapperDbo.toModel(user);
    }

    @Override
    public void saveRefreshToken(String refreshToken, String username) {
        System.out.println(username);
        UserEntity user = this.userRepositoryJpa.findByUsername(username)
                .orElseThrow();

        user.setRefreshToken(refreshToken);
        this.userRepositoryJpa.save(user);
    }

    @Override
    public List<UserCompetenceDTO> getAvailableUsersWithCompetencies() {
        return userRepositoryJpa.findByAvailableTrue().stream()
                .map(proy -> UserCompetenceDTO.builder()
                        .userId(proy.getUserId())
                        .name(proy.getName())
                        .position(proy.getPosition())
                        .imageProfile(proy.getImageProfile())
                        .qualifiedAnalyses(proy.getTrainedAnalyses().stream()
                                .map(a -> new UserCompetenceDTO.AnalysisDTO(a.getAnalysisId(), a.getAnalysisName()))
                                .toList())
                        .build())
                .toList();
    }

    @Override
    public List<TestRequestModel> findAllTestRequestByUser(Long userId) {
        List<TestRequestEntity> testRequestEntities = this.userRepositoryJpa.findAllTestRequestByUser(userId);
        return testRequestEntities.stream().map(this.testRequestMapperDbo::toModel).toList();


    }

    @Override
    public void deleteRefreshToken(String username) {
        System.out.println("Se elimino el token brou");
        System.out.println("juannnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn" +
                "sdsd" +
                "sdsdsd" +
                "sdsdsdsdsd." +
                "." +
                "." +
                "," +
                ".");
        UserEntity user = this.userRepositoryJpa.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("No se encontro el usuario"));

        user.setRefreshToken("");
        this.userRepositoryJpa.save(user);
    }

    @Override
    public UserModel findByEmail(String email) {
        UserEntity user = this.userRepositoryJpa.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("No se encontro el usuario"));
        return this.userMapperDbo.toModel(user);
    }

    @Override
    public UserEntity findEntityById(Long userId) {
        return this.userRepositoryJpa.findById(userId).orElseThrow(() -> new UsernameNotFoundException("No se pudo encontrar el usuario"));
    }

    @Override
    public boolean existByEmail(String email) {
        return this.userRepositoryJpa.existsByEmail(email);
    }

    @Override
    public List<UserModel> findAllById(List<Long> listOfId) {
        List<UserEntity> all = this.userRepositoryJpa.findAllById(listOfId);
        return all.stream().map(this.userMapperDbo::toModel).toList();
    }
}
