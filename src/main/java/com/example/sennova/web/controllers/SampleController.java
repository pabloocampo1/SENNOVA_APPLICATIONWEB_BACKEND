package com.example.sennova.web.controllers;

import com.example.sennova.application.dto.testeRequest.ReceptionInfoRequest;
import com.example.sennova.application.dto.testeRequest.SampleData;
import com.example.sennova.application.usecases.SampleUseCase;
import com.example.sennova.domain.model.testRequest.SampleModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @PostMapping(value = "/save-reception",  consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<SampleModel> saveReceptionInfo(
            @RequestPart("dto") ReceptionInfoRequest receptionInfoRequest,
            @RequestPart(value = "file", required = false) MultipartFile file,
            @RequestParam("sampleId") Long sampleId )
    {
        return new ResponseEntity<>(this.sampleUseCase.saveReception(receptionInfoRequest, sampleId , file), HttpStatus.OK);
    }
}
