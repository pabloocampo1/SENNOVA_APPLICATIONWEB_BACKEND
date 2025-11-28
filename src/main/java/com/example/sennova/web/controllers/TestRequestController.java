package com.example.sennova.web.controllers;

import com.example.sennova.application.dto.UserDtos.UserResponse;
import com.example.sennova.application.dto.testeRequest.*;
import com.example.sennova.domain.model.testRequest.SampleModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.sennova.application.usecases.TestRequestUseCase;
import com.example.sennova.domain.model.testRequest.TestRequestModel;

import jakarta.validation.Valid;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/testRequest")
public class TestRequestController {

    private final TestRequestUseCase testRequestUseCase;

    @Autowired
    public TestRequestController(TestRequestUseCase testRequestUseCase){
        this.testRequestUseCase = testRequestUseCase;
    }

    @GetMapping("/get-all/quotation")
    public ResponseEntity<List<TestRequestModel>> getAll(){
        return new ResponseEntity<>(this.testRequestUseCase.getAllTestRequest(), HttpStatus.OK);
    }
    
    @GetMapping("/get-by-id/{testRequestId}")
    public ResponseEntity<TestRequestModel> getById(@PathVariable("testRequestId") Long id){
        return new ResponseEntity<>(this.testRequestUseCase.getTestRequestById(id), HttpStatus.OK);
    }

    @GetMapping("/get-by-requestCode/{requestCode}")
    public ResponseEntity<TestRequestModel> getByRequestCode(@PathVariable("requestCode") Long id){
        return new ResponseEntity<>(this.testRequestUseCase.getTestRequestById(id), HttpStatus.OK);
    }


    @PostMapping("/assign-members")
    public ResponseEntity<Void> assignMembers(@RequestBody Map<String, Object> object){

        Long testRequestId = ((Number) object.get("testRequestId")).longValue();
        List<Long> users = (List<Long>) object.get("users");

         this.testRequestUseCase.assignResponsible(users, testRequestId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/get-all/accepted")
    public ResponseEntity<List<TestRequestModel>> getAllTestRequestAccepted(){
        return new ResponseEntity<>(this.testRequestUseCase.getAllTestRequestAccepted(), HttpStatus.OK);
    }

    @GetMapping("/members/{testRequestId}")
    public ResponseEntity<List<UserResponse>> getAllMembers(@PathVariable("testRequestId") Long testRequest){
        return new ResponseEntity<>(this.testRequestUseCase.usersAssignedTestRequest(testRequest), HttpStatus.OK);
    }

    @GetMapping("/get-samples-by-testRequestId/{testRequestId}")
    public ResponseEntity<Map<String, List<SamplesByTestRequestDto>>> getSamplesByTestRequest(@PathVariable("testRequestId") Long testRequestId) {
        return new ResponseEntity<>(this.testRequestUseCase.getSamplesByTestRequest(testRequestId), HttpStatus.OK);
    }


    @GetMapping("/get-by-state/{state}")
    public ResponseEntity<List<TestRequestModel>> getAllByState(@PathVariable("state") String state) {
        return new ResponseEntity<>(this.testRequestUseCase.getAllByState(state), HttpStatus.OK);
    }

    @GetMapping("/get-by-option-search/{option}/{param}")
    public ResponseEntity<List<TestRequestModel>> getAllByOptionSearch(@PathVariable("option") String option, @PathVariable("param") String param) {
        return new ResponseEntity<>(this.testRequestUseCase.getAllByOptionAndParam(option, param), HttpStatus.OK);
    }

    @PutMapping("/accept-or-reject-test-request")
    public ResponseEntity<TestRequestModel> acceptOrRejectTestRequest(@RequestBody AcceptOrRejectTestRequestDto dto)  {
            return new ResponseEntity<>(this.testRequestUseCase.acceptOrRejectTestRequest(dto.testRequestId(), dto.isApproved(), dto.message(), dto.emailCustomer()),HttpStatus.OK);
    }
    
    @PostMapping("/quotation")
    public ResponseEntity<TestRequestModel> saveQuotation(@RequestBody TestRequestRecord testRequestRecord) {
        
        TestRequestModel testRequestModel = this.testRequestUseCase.save(testRequestRecord);
        return new ResponseEntity<>(testRequestModel, HttpStatus.CREATED);
    }
    


    @DeleteMapping("/delete-by-id/{testRequestId}")
    public ResponseEntity<Void> deleteTestRequestById(@PathVariable("testRequestId") Long testRequestId) {
        this.testRequestUseCase.deleteById(testRequestId);
        return new ResponseEntity<>(HttpStatus.OK);

    }

    @GetMapping("/get-all-info-summary")
    public ResponseEntity<List<TestRequestSummaryInfoResponse>> getTestRequestSummaryInfo(){
           return new ResponseEntity<>(this.testRequestUseCase.getAllTestRequestSummaryInfo(), HttpStatus.OK);
    }

    @GetMapping("/get-all-info-summary-by-code/{requestCode}")
    public ResponseEntity<List<TestRequestSummaryInfoResponse>> getTestRequestSummaryInfoByCode(@PathVariable("requestCode") String code){
        return new ResponseEntity<>(this.testRequestUseCase.getAllTestRequestSummaryInfoByCode(code), HttpStatus.OK);
    }

    @GetMapping("/get-all-info-summary-by-status/{status}")
    public ResponseEntity<List<TestRequestSummaryInfoResponse>> getTestRequestSummaryInfoByStatus(@PathVariable("status") String status){
        return new ResponseEntity<>(this.testRequestUseCase.getAllTestRequestSummaryInfoByDeliveryState(status), HttpStatus.OK);
    }

    @PutMapping("/remove-member/{testRequestId}/{userId}")
    public ResponseEntity<List<UserResponse>> removeMember(@PathVariable("userId") Long userId, @PathVariable("testRequestId") Long testRequestId){
        this.testRequestUseCase.removeMember(userId, testRequestId);
        return new ResponseEntity<>(this.testRequestUseCase.removeMember(userId, testRequestId), HttpStatus.OK);
    }

    // this method was created only for manual test
   /*
    @GetMapping("/get-all-samples")
    public ResponseEntity<List<SampleModel>> getSamples(){
        return new ResponseEntity<>(this.testRequestUseCase.getSamples(), HttpStatus.OK);
    } */




}
