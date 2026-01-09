package com.example.sennova.web.controllers;

import com.example.sennova.application.dto.UserDtos.UserResponseMembersAssigned;
import com.example.sennova.application.dto.testeRequest.*;
import com.example.sennova.application.dto.testeRequest.ReleaaseResult.InfoResponsiblePersonReleaseResult;
import com.example.sennova.application.dto.testeRequest.ReleaaseResult.SendReportSamplesDto;
import com.example.sennova.application.dto.testeRequest.quotation.QuotationResponse;
import com.example.sennova.application.dto.testeRequest.sample.SamplesByTestRequestDto;
import com.example.sennova.application.usecases.TestRequest.TestRequestReleaseResultUseCase;
import com.example.sennova.application.usecasesImpl.ReleaseResultGeneratePdfService;
import com.example.sennova.domain.model.testRequest.SampleModel;
import com.example.sennova.infrastructure.persistence.entities.analysisRequestsEntities.ReportDeliverySample;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.sennova.application.usecases.TestRequest.TestRequestUseCase;
import com.example.sennova.domain.model.testRequest.TestRequestModel;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/testRequest")
public class TestRequestController {

    private final TestRequestUseCase testRequestUseCase;
    private final TestRequestReleaseResultUseCase testRequestReleaseResultUseCase;

    @Autowired
    public TestRequestController(TestRequestUseCase testRequestUseCase, TestRequestReleaseResultUseCase testRequestReleaseResultUseCase) {
        this.testRequestUseCase = testRequestUseCase;
        this.testRequestReleaseResultUseCase = testRequestReleaseResultUseCase;
    }

    @GetMapping("/get-all/quotation")
    public ResponseEntity<Page<QuotationResponse>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int elements
    ){
        Pageable pageable = PageRequest.of(page, elements, Sort.by("createAt").descending());
        return new ResponseEntity<>(this.testRequestUseCase.getAllQuotation(pageable), HttpStatus.OK);
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
    public ResponseEntity<List<UserResponseMembersAssigned>> getAllMembers(@PathVariable("testRequestId") Long testRequest){
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
    public ResponseEntity<Page<TestRequestSummaryInfoResponse>> getTestRequestSummaryInfo(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int elements
    ){
        Pageable pageable = PageRequest.of(page, elements);
           return new ResponseEntity<>(this.testRequestUseCase.getAllTestRequestSummaryInfo(pageable), HttpStatus.OK);
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
    public ResponseEntity<List<UserResponseMembersAssigned>> removeMember(@PathVariable("userId") Long userId, @PathVariable("testRequestId") Long testRequestId){
        this.testRequestUseCase.removeMember(userId, testRequestId);
        return new ResponseEntity<>(this.testRequestUseCase.removeMember(userId, testRequestId), HttpStatus.OK);
    }


    @PostMapping("/pdf/preview/{sampleId}")
    public ResponseEntity<byte[]> generatePdf(
            @PathVariable("sampleId") Long sampleId
            ) {
        try {
            
            byte[] releaseResult = this.testRequestReleaseResultUseCase.generateReleaseResultBySampleIdPreview(sampleId);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=resultados.pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    
                    .body(releaseResult);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    @PostMapping("/send-report-samples")
    public ResponseEntity<Void> sendReportSamples(
            @RequestParam("samples") String samplesJson,
            @RequestParam("signature") MultipartFile signature,
            @RequestParam("name") String name,
            @RequestParam("role") String role
    ) throws Exception {
        List<Long> samples = new ObjectMapper().readValue(samplesJson, new TypeReference<List<Long>>(){});
        InfoResponsiblePersonReleaseResult info = new InfoResponsiblePersonReleaseResult();
        info.setName(name);
        info.setRole(role);
        info.setSignature(signature);

        testRequestReleaseResultUseCase.generateAndSendSampleReport(samples, info);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/finish-test-request")
    public ResponseEntity<Void>  sendResultTestRequest(
            @RequestParam("testRequestId") Long testRequestId,
            @RequestParam(value = "notes", required = false) String notes,
            @RequestParam(value = "documents", required = false) List<MultipartFile> documents,
            @RequestParam(value = "responsibleName") String responsibleName,
            @RequestParam(value = "signature") MultipartFile signatureImage

    ){
        ResultExecutionFinalTestRequestDto finalReport = new ResultExecutionFinalTestRequestDto(
               testRequestId,
               notes,
               documents,
               responsibleName,
               signatureImage
        );




        return new ResponseEntity<>( HttpStatus.OK);
    }

    @GetMapping("/delivery-history/{requestCode}")
    public ResponseEntity<List<ReportDeliverySample>> getDeliveryHistoryByRequestCode(@PathVariable("requestCode") String requestCode){
        return new ResponseEntity<>(this.testRequestReleaseResultUseCase.getHistoryDeliveryByRequestCode(requestCode), HttpStatus.OK);
    }





}
