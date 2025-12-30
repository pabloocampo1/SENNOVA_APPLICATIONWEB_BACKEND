package com.example.sennova.web.controllers;

import com.example.sennova.application.dto.UserDtos.UserResponseMembersAssigned;
import com.example.sennova.application.dto.testeRequest.*;
import com.example.sennova.application.dto.testeRequest.quotation.QuotationResponse;
import com.example.sennova.application.dto.testeRequest.sample.SamplesByTestRequestDto;
import com.example.sennova.application.usecasesImpl.PdfService;
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

import com.example.sennova.application.usecases.TestRequestUseCase;
import com.example.sennova.domain.model.testRequest.TestRequestModel;



import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/testRequest")
public class TestRequestController {

    private final TestRequestUseCase testRequestUseCase;
    private final PdfService pdfService;

    @Autowired
    public TestRequestController(TestRequestUseCase testRequestUseCase, PdfService pdfService){
        this.testRequestUseCase = testRequestUseCase;
        this.pdfService = pdfService;
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


    @GetMapping("/pdf/preview")
    public ResponseEntity<byte[]> generatePdf() {
        try {
            byte[] pdfBytes = pdfService.generatePdfFinalResultTestRequest();


            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=resultados.pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdfBytes);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }





}
