package com.example.sennova.infrastructure.persistence.repositoryJpa;

import com.example.sennova.domain.model.UserModel;
import com.example.sennova.domain.model.testRequest.TestRequestModel;
import com.example.sennova.infrastructure.persistence.entities.UserEntity;
import com.example.sennova.infrastructure.persistence.entities.requestsEntities.TestRequestEntity;
import com.example.sennova.infrastructure.projection.UserWithAnalysisProjection;
import org.springframework.data.jpa.repository.EntityGraph;
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


    @Query(value = """
    SELECT a.analysis_name 
    FROM analysis a
    INNER JOIN sample_analysis sa ON a.analysis_id = sa.analysis_id
    INNER JOIN samples s ON sa.sample_id = s.sample_id
    WHERE s.test_request_id = :requestId
    AND a.analysis_id NOT IN (
        SELECT ar.analysis_id 
        FROM analysis_responsible ar 
        WHERE ar.user_id = :userId
    )
    """, nativeQuery = true)
    List<String> findUnauthorizedAnalyses(@Param("requestId") Long requestId, @Param("userId") Long userId);

    @EntityGraph(attributePaths = {"trainedAnalyses"})
    List<UserWithAnalysisProjection> findByAvailableTrue();



    // get the test requets by user
    @Query(value = "SELECT t.* FROM test_request t INNER JOIN test_user tu ON tu.test_request_id = t.test_request_id\n" +
            "WHERE tu.user_id = :userId;", nativeQuery = true)
    List<TestRequestEntity> findAllTestRequestByUser(@Param("userId") Long userId);
}
