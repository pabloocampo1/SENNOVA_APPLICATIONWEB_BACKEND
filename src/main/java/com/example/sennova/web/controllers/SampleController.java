package com.example.sennova.web.controllers;

import com.example.sennova.application.dto.testeRequest.ReceptionInfoRequest;
import com.example.sennova.application.dto.testeRequest.SampleAnalysisRequestRecord;
import com.example.sennova.application.dto.testeRequest.SampleData;
import com.example.sennova.application.usecases.SampleUseCase;
import com.example.sennova.domain.model.testRequest.SampleAnalysisModel;
import com.example.sennova.domain.model.testRequest.SampleModel;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("/get-expired")
    public ResponseEntity<List<SampleModel>> getAllExpired(){
        return  new ResponseEntity<>(this.sampleUseCase.getAllByStatusReception(), HttpStatus.OK);
    }


    @PostMapping( value = "/save-result", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<SampleAnalysisModel> saveResult(
            @RequestPart("dto") SampleAnalysisRequestRecord sampleAnalysisRequestRecord,
            @RequestPart(value = "files", required = false) List<MultipartFile> files ,
            @RequestPart("testRequestId") String requestCode
    ){
        return  new ResponseEntity<>(this.sampleUseCase.saveResult(  sampleAnalysisRequestRecord,  files, requestCode), HttpStatus.OK);
    }

    @PostMapping(value = "/save-reception",  consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<SampleModel> saveReceptionInfo(
            @RequestPart("dto") ReceptionInfoRequest receptionInfoRequest,
            @RequestPart(value = "file", required = false) MultipartFile file,
            @RequestParam("sampleId") Long sampleId )
    {
        return new ResponseEntity<>(this.sampleUseCase.saveReception(receptionInfoRequest, sampleId , file), HttpStatus.OK);
    }
}
