package com.example.sennova.web.controllers;

import com.example.sennova.application.dto.inventory.ReagentInventory.ReagentRequestDto;
import com.example.sennova.application.dto.inventory.ReagentInventory.ReagentResponseDto;
import com.example.sennova.application.dto.inventory.ReagentInventory.ReagentSummaryStatistics;
import com.example.sennova.application.dto.inventory.ReagentInventory.UsageReagentRequest;
import com.example.sennova.application.mapper.ReagentMapper;
import com.example.sennova.application.usecases.ReagentUseCase;
import com.example.sennova.domain.model.ReagentModel;
import com.example.sennova.infrastructure.persistence.entities.inventoryReagentsEntities.ReagentMediaFilesEntity;
import com.example.sennova.infrastructure.persistence.entities.inventoryReagentsEntities.ReagentsUsageRecords;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reagent")
public class ReagentController {

    private final ReagentUseCase reagentUseCase;
    private final ReagentMapper reagentMapper;

    @Autowired
    public ReagentController(ReagentUseCase reagentUseCase, ReagentMapper reagentMapper) {
        this.reagentUseCase = reagentUseCase;
        this.reagentMapper = reagentMapper;
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<ReagentResponseDto>> getAll() {
        List<ReagentModel> reagentModels = this.reagentUseCase.getAll();
        return new ResponseEntity<>(reagentModels.stream().map(this.reagentMapper::toResponse).toList(), HttpStatus.OK);
    }

    @GetMapping("/get-summary-inventory")
    public ResponseEntity<ReagentSummaryStatistics> getSummaryInventory() {
        return new ResponseEntity<>(this.reagentUseCase.getSummaryStatics(), HttpStatus.OK);
    }

    @GetMapping("/getAll/page")
    public ResponseEntity<Page<ReagentResponseDto>> getAllPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int elements
    ) {
        Pageable pageable = PageRequest.of(page, elements);
        Page<ReagentModel> reagentModels = this.reagentUseCase.getAll(pageable);
        return new ResponseEntity<>(reagentModels.map(this.reagentMapper::toResponse), HttpStatus.OK);
    }

    @GetMapping("/getAllByLocation/{locationId}")
    public ResponseEntity<List<ReagentResponseDto>> getAllByLocation(@PathVariable("locationId") Long id) {
        List<ReagentModel> reagents = this.reagentUseCase.getAllByLocation(id);
        return new ResponseEntity<>(reagents.stream().map(this.reagentMapper::toResponse).toList(), HttpStatus.OK);
    }

    @GetMapping("/getAllByName/{name}")
    public ResponseEntity<List<ReagentResponseDto>> getAllByName(@PathVariable("name") String name) {
        List<ReagentModel> reagents = this.reagentUseCase.getAllByName(name);
        return new ResponseEntity<>(reagents.stream().map(this.reagentMapper::toResponse).toList(), HttpStatus.OK);
    }

    @GetMapping("/get-files/{reagentId}")
    public ResponseEntity<List<ReagentMediaFilesEntity>> getFiles(@PathVariable("reagentId") Long reagentId) {
        List<ReagentMediaFilesEntity> files = this.reagentUseCase.getFiles(reagentId);
        return new ResponseEntity<>(files, HttpStatus.OK);
    }

    @GetMapping("/get-usages/{reagentId}")
    public ResponseEntity<List<ReagentsUsageRecords>> getUsages(@PathVariable("reagentId") Long reagentId) {
        List<ReagentsUsageRecords> files = this.reagentUseCase.getUsagesByReagentId(reagentId);
        return new ResponseEntity<>(files, HttpStatus.OK);
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<ReagentResponseDto> getAllByName(@PathVariable("id") Long id) {
        ReagentModel reagent = this.reagentUseCase.getById(id);
        return new ResponseEntity<>(this.reagentMapper.toResponse(reagent), HttpStatus.OK);
    }

    @PostMapping(path = "/save/{performedBy}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ReagentResponseDto> save(
            @RequestPart("dto") ReagentRequestDto reagentRequestDto,
            @RequestPart(name = "image", required = false) MultipartFile imageFile,
            @PathVariable(name = "performedBy") String performedBy
    ) {

        ReagentModel reagentModel = this.reagentMapper.toModel(reagentRequestDto);
        ReagentModel reagentSaved = this.reagentUseCase.save(
                reagentModel,
                imageFile,
                reagentRequestDto.locationId(),
                reagentRequestDto.usageId()   ,
                performedBy
        );

        return new ResponseEntity<>(this.reagentMapper.toResponse(reagentSaved), HttpStatus.CREATED);

    }

    @PutMapping(path = "/update/{reagentId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ReagentResponseDto> update(
            @RequestPart("dto") ReagentRequestDto reagentRequestDto,
            @RequestPart(name = "image", required = false) MultipartFile imageFile,
            @PathVariable(name = "reagentId") Long reagentId
    ) {

        ReagentModel reagentModel = this.reagentMapper.toModel(reagentRequestDto);
        ReagentModel reagentSaved = this.reagentUseCase.update(
                reagentModel,
                reagentId,
                imageFile,
                reagentRequestDto.locationId(),
                reagentRequestDto.usageId() ,
                "Sistema"

        );

        return new ResponseEntity<>(this.reagentMapper.toResponse(reagentSaved), HttpStatus.OK);

    }

    @PostMapping(path = "/upload-files/{reagentId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<List<ReagentMediaFilesEntity>> uploadFiles(
            @RequestPart(name = "files") List<MultipartFile> files,
            @PathVariable("reagentId") Long reagentId
    ) {
        List<ReagentMediaFilesEntity> filesEntity = this.reagentUseCase.uploadFiles(reagentId, files);
        return new ResponseEntity<>(filesEntity, HttpStatus.CREATED);
    }

    @PostMapping(path = "/save-usage")
    public ResponseEntity<ReagentsUsageRecords> saveUsage(
           @Valid @RequestBody UsageReagentRequest usage
    ) {
        return new ResponseEntity<>(this.reagentUseCase.saveUsage(usage), HttpStatus.CREATED);
    }

    @PutMapping("/change-state/{reagentId}/{state}")
    public ResponseEntity<ReagentResponseDto> changeState(
            @PathVariable("reagentId") Long reagentId,
            @PathVariable("state") String state

    ) {
        ReagentModel reagentModel = this.reagentUseCase.changeState(reagentId, state);
        return new ResponseEntity<>(this.reagentMapper.toResponse(reagentModel), HttpStatus.OK);
    }

    @PutMapping("/change-quantity/{reagentId}/{quantity}")
    public ResponseEntity<ReagentResponseDto> changeQuantity(
            @PathVariable("reagentId") Long reagentId,
            @PathVariable("quantity") Integer quantity

    ) {
        ReagentModel reagentModel = this.reagentUseCase.changeQuantity(reagentId, quantity);
        return new ResponseEntity<>(this.reagentMapper.toResponse(reagentModel), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        this.reagentUseCase.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/delete-file/{publicId}")
    public ResponseEntity<Void> deleteFile(@PathVariable("publicId") String id) {
        this.reagentUseCase.deleteFile(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
