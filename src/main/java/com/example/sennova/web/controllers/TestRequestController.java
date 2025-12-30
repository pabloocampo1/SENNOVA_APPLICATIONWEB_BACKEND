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
import org.springframework.web.multipart.MultipartFile;


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

            // crear un servicio en sample donde retorne el pdf, este debe de recibir el sampleId para retornar soo el pdf de un sample, ademas de cargar los demas datos del ensayo y cliente

            List<PdfService.ResultadoAnalisis> listaResultados = List.of(
                    new PdfService.ResultadoAnalisis(1, "pH", "SM 4500", "7,35", "NA", "6,50 – 9,00", "2025-07-31"),
                    new PdfService.ResultadoAnalisis(2, "Cloro residual", "SM 4500", "0,06 mg/L", "NA", "0,3 – 2,0 mg/L", "2025-07-31")
            );

            PdfService.DatosInformeDTO miInforme = new PdfService.DatosInformeDTO(
                    "JUAN PEREZ",
                    "EMBOTELLADORA POOL S.A.S",      
                    "Calle 10 # 45-20, Medellín",
                    "900.123.456-1",
                    "contacto@pool.com.co",
                    "COT-2025-089",
                    "6041234567",
                    "2025-07-31",
                    "2025-08-04",
                    "Jonathan Henao Valencia",
                    listaResultados
            );

            byte[] pdfBytes = pdfService.generarInformePdf(
                   miInforme,
                    null

            );


            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=resultados.pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdfBytes);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
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

        this.testRequestUseCase.sendFinalReport(finalReport);


        return new ResponseEntity<>( HttpStatus.OK);
    }





}
