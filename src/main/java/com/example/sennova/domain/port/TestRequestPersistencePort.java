package com.example.sennova.domain.port;

import com.example.sennova.application.dto.testeRequest.CustomerResponse;
import com.example.sennova.application.usecases.TestRequestUseCase;
import com.example.sennova.domain.model.UserModel;
import com.example.sennova.domain.model.testRequest.CustomerModel;
import com.example.sennova.domain.model.testRequest.TestRequestModel;
import com.example.sennova.infrastructure.persistence.entities.analysisRequestsEntities.TestRequestEntity;
import com.example.sennova.infrastructure.projection.SampleInfoSummaryTestRequestProjection;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TestRequestPersistencePort {
    TestRequestModel save(TestRequestModel testRequestModel);
    TestRequestEntity saveEntity(TestRequestEntity testRequestEntity);

    List<TestRequestModel> getAll();
    Boolean existsById(Long testRequestId);
    List<SampleInfoSummaryTestRequestProjection> findSamplesInfoByTestRequest(Long testRequestId);
    void deleteById(Long testRequestId);
    Optional<TestRequestModel> findById(Long id);
    List<TestRequestModel> findAllByState(String state);
    Optional<TestRequestModel> findByRequestCode(String requestCode);
    List<TestRequestModel> findAllByCustomerName(String customerName);
    List<TestRequestModel> findAllByRequestCode(String requestCode);
    List<TestRequestModel> findAllByDueDate(LocalDate today);
    List<TestRequestModel> findAllTestRequestAccepted();
    List<TestRequestModel> findAllByDueDateExpired(LocalDate today);
    List<TestRequestModel> findAllByDeliveryState(String state);
    List<TestRequestModel> findAllTestRequestByRequestCode(String code);
    void assignResponsible(Long testRequestId, List<UserModel> users);
    Optional<TestRequestEntity> getWithSamplesAndAnalysis(String requestCode);
    void removeMember(Long userId, Long testRequestId);


}
