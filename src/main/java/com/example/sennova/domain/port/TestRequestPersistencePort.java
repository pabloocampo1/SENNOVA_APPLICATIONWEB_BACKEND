package com.example.sennova.domain.port;

import com.example.sennova.application.dto.testeRequest.CustomerResponse;
import com.example.sennova.application.usecases.TestRequestUseCase;
import com.example.sennova.domain.model.UserModel;
import com.example.sennova.domain.model.testRequest.CustomerModel;
import com.example.sennova.domain.model.testRequest.TestRequestModel;
import com.example.sennova.infrastructure.projection.SampleInfoSummaryTestRequestProjection;

import java.util.List;
import java.util.Optional;

public interface TestRequestPersistencePort {
    TestRequestModel save(TestRequestModel testRequestModel);
    List<TestRequestModel> getAll();
    Boolean existsById(Long testRequestId);
    List<SampleInfoSummaryTestRequestProjection> findSamplesInfoByTestRequest(Long testRequestId);
    void deleteById(Long testRequestId);
    Optional<TestRequestModel> findById(Long id);
    List<TestRequestModel> findAllByState(String state);
    Optional<TestRequestModel> findByRequestCode(String requestCode);
    List<TestRequestModel> findAllByCustomerName(String customerName);
    List<TestRequestModel> findAllByRequestCode(String requestCode);
    List<TestRequestModel> findAllTestRequestAccepted();
    List<TestRequestModel> findAllByDeliveryState(String state);
    List<TestRequestModel> findAllTestRequestByRequestCode(String code);
    void assignResponsible(Long testRequestId, List<UserModel> users);
    void removeMember(Long userId, Long testRequestId);


}
