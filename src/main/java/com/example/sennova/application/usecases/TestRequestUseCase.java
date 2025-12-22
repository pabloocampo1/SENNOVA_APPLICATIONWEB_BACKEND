package com.example.sennova.application.usecases;

import java.util.List;
import java.util.Map;

import com.example.sennova.application.dto.UserDtos.UserResponse;
import com.example.sennova.application.dto.UserDtos.UserResponseMembersAssigned;
import com.example.sennova.application.dto.testeRequest.*;
import com.example.sennova.application.dto.testeRequest.sample.SamplesByTestRequestDto;
import com.example.sennova.domain.model.testRequest.SampleModel;
import com.example.sennova.domain.model.testRequest.TestRequestModel;

public interface TestRequestUseCase {
    List<TestRequestModel> getAllTestRequest();
    List<TestRequestModel> getAllTestRequestAccepted();
    void updateStatus(TestRequestModel testRequestModel);
    TestRequestModel save(TestRequestRecord testRequestRecord);
    TestRequestModel update(TestRequestModel testRequestModel);
    TestRequestModel getTestRequestById(Long id);
    TestRequestModel getByRequestCode(String requestCode);
    List<TestRequestModel> getAllTestRequestNoCheck();
    List<TestRequestModel> getTestRequestsDueToday();
    List<TestRequestModel> getTestRequestDueDateExpired();
    List<TestRequestModel> getAllByState(String state);
    List<TestRequestModel> getTestRequestByCode();
    Map<String, List<SamplesByTestRequestDto>> getSamplesByTestRequest(Long testRequestId);
    void deleteById(Long testRequestId);
    TestRequestModel acceptOrRejectTestRequest(Long testRequestId, Boolean isApproved, String message, String emailCustomer);
    List<TestRequestModel> getAllByOptionAndParam(String option, String param);
    void assignResponsible(List<Long> usersId, Long testRequestId);
    List<TestRequestSummaryInfoResponse> getAllTestRequestSummaryInfo();
    List<TestRequestSummaryInfoResponse> getAllTestRequestSummaryInfoByCode(String code);
    List<TestRequestSummaryInfoResponse> getAllTestRequestSummaryInfoByDeliveryState(String state);
    List<SampleModel> getSamples();
    List<UserResponseMembersAssigned> usersAssignedTestRequest(Long testRequestId);
    List<UserResponseMembersAssigned> removeMember(Long userId, Long testRequestId);
    void changeStatusDelivery( Long testRequestId);

}
