package com.example.sennova.web.controllers;

import com.example.sennova.application.dto.testeRequest.ReceptionInfoRequest;
import com.example.sennova.application.dto.testeRequest.SampleAnalysisRequestRecord;
import com.example.sennova.application.dto.testeRequest.SampleData;
import com.example.sennova.application.dto.testeRequest.SampleInfoExecutionDto;
import com.example.sennova.application.dto.testeRequest.sample.SampleDeliveredResponse;
import com.example.sennova.application.dto.testeRequest.sample.SampleWithoutReceptionResponse;
import com.example.sennova.application.usecases.SampleUseCase;
import com.example.sennova.domain.model.testRequest.SampleAnalysisModel;
import com.example.sennova.domain.model.testRequest.SampleModel;
import com.example.sennova.infrastructure.persistence.entities.analysisRequestsEntities.SampleProductDocumentResult;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/sample")
public class SampleController {

    private final SampleUseCase sampleUseCase;

    @Autowired
    public SampleController(SampleUseCase sampleUseCase) {
        this.sampleUseCase = sampleUseCase;
    }
    @GetMapping("/get-sample-data/{sampleId}")
    public ResponseEntity<SampleModel> getData(@PathVariable("sampleId") Long sampleId){
         return new ResponseEntity<>( this.sampleUseCase.getById(sampleId), HttpStatus.OK);
    }

    @GetMapping("/get-all-status-process")
    public ResponseEntity<List<SampleModel>> getAllSamplesNoFinished(){
         return new ResponseEntity<>(this.sampleUseCase.getAllByStatusReception(), HttpStatus.OK);
    }


    @GetMapping("/get-all-delivered")
    public ResponseEntity<Page<SampleDeliveredResponse>> getAllSamplesDelivered(@RequestParam(defaultValue = "0") int page, @RequestParam( defaultValue = "20") int elements){
        Pageable pageable = PageRequest.of(page, elements, Sort.by("deliveryDate").descending());
        return new ResponseEntity<>(this.sampleUseCase.getAllSamplesDelivered(pageable), HttpStatus.OK);
    }

    @GetMapping("/get-all-without-reception")
    public ResponseEntity<Page<SampleWithoutReceptionResponse>> getAllSamplesWithoutReception(@RequestParam(defaultValue = "0") int page, @RequestParam( defaultValue = "20") int elements){
        Pageable pageable = PageRequest.of(page, elements, Sort.by("dueDate").descending());
        return new ResponseEntity<>(this.sampleUseCase.getAllSamplesWithoutReception(pageable), HttpStatus.OK);
    }

    @GetMapping("/get-all-status-expired")
    public ResponseEntity<List<SampleModel>> getAllSamplesExpired(){
        return new ResponseEntity<>(this.sampleUseCase.getAllByStatusExpired(), HttpStatus.OK);
    }
    

    @PostMapping( value = "/save-result", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<SampleAnalysisModel> saveResult(
            @RequestPart("dto") SampleAnalysisRequestRecord sampleAnalysisRequestRecord,
            @RequestPart("testRequestId") String requestCode
    ){
        return  new ResponseEntity<>(this.sampleUseCase.saveResult(  sampleAnalysisRequestRecord,  requestCode), HttpStatus.OK);
    }

    @PostMapping(value = "/save-reception",  consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<SampleModel> saveReceptionInfo(
            @RequestPart("dto") ReceptionInfoRequest receptionInfoRequest,
            @RequestPart(value = "file", required = false) MultipartFile file,
            @RequestParam("sampleId") Long sampleId )
    {
        return new ResponseEntity<>(this.sampleUseCase.saveReception(receptionInfoRequest, sampleId , file), HttpStatus.OK);
    }

    @PostMapping(value = "/save-document-analysis", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<List<SampleProductDocumentResult>> saveDocumentResult(
            @RequestPart("docs") List<MultipartFile> docs,
            @RequestParam("analysisResultId") Long analysisResultId
    ){
        return new ResponseEntity<>(this.sampleUseCase.saveDocsResult(docs, analysisResultId),HttpStatus.CREATED);
    }

    @DeleteMapping("/delete-file-result/{sampleProductDocumentResultId}")
    public ResponseEntity<Void> deleteFile(@PathVariable("sampleProductDocumentResultId") Long sampleProductDocumentResultId){
        this.sampleUseCase.deleteFileResultAnalysis(sampleProductDocumentResultId);
           return new ResponseEntity<>( HttpStatus.OK);
    }

    @GetMapping("/get-sample-info-execution")
    public ResponseEntity<List<SampleInfoExecutionDto>> getSamplesInfoExecution(
            @RequestParam List<Long> samplesId
    ){
       return new ResponseEntity<>(this.sampleUseCase.getSamplesInfoExecution(samplesId), HttpStatus.OK);
    }
}
