package com.example.sennova.infrastructure.persistence.repositoryJpa;

import com.example.sennova.domain.model.UserModel;
import com.example.sennova.infrastructure.persistence.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepositoryJpa extends JpaRepository<UserEntity, Long> {

    boolean existsByUsername(String username);

    @Query(value = "SELECT * FROM users WHERE username = :username", nativeQuery = true)
    Optional<UserEntity> findByUsername(@Param("username") String username);


    List<UserEntity> findAllByNameContainingIgnoreCase(String name);
    List<UserEntity> findAllByDni(Long dni);

    @Query("SELECT u FROM UserEntity u WHERE u.role.roleId = :roleId")
    List<UserEntity> findAllByRole(@Param("roleId") Long roleId);

    Optional<UserEntity> findByEmail(String email);

    List<UserEntity> findAllByAvailableTrue();

    @Query("SELECT u FROM UserEntity u JOIN u.testRequestEntities t WHERE t.testRequestId = :testRequestId")
    List<UserEntity> findAllUserByTestRequestId(@Param("testRequestId") Long testRequestId);


    @Query(
            value = """
    SELECT u.*
    FROM users u
    INNER JOIN test_user t ON u.user_id = t.user_id
    WHERE t.test_request_id = :testRequestId
  """,
            nativeQuery = true
    )
    List<UserEntity> findAllByTestRequest(@Param("testRequestId") Long testRequestId);


    boolean existsByEmail(String email);

}
