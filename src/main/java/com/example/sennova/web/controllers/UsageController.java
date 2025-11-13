package com.example.sennova.web.controllers;

import com.example.sennova.application.dto.inventory.UsageRequestDto;
import com.example.sennova.application.dto.inventory.UsageResponseDto;
import com.example.sennova.application.mapper.UsageMapper;
import com.example.sennova.application.usecases.UsageUseCase;
import com.example.sennova.domain.model.UsageModel;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/usage")
public class UsageController {

    private final UsageUseCase usageUseCase;
    private final UsageMapper usageMapper;

    @Autowired
    public UsageController(UsageUseCase usageUseCase, UsageMapper usageMapper) {
        this.usageUseCase = usageUseCase;
        this.usageMapper = usageMapper;
    }


    @PostMapping("/save")
    public ResponseEntity<UsageResponseDto> save(@RequestBody @Valid UsageRequestDto usageRequestDto){
        UsageModel equipmentLocationModel = this.usageMapper.toModel(usageRequestDto);
        UsageModel usageModelSaved = this.usageUseCase.save(equipmentLocationModel);
        return new ResponseEntity<>(this.usageMapper.toResponse(usageModelSaved), HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<UsageResponseDto> update(
            @RequestBody @Valid UsageRequestDto usageRequestDto,
            @PathVariable("id") @Valid Long id) {
        UsageModel equipmentLocationModel = this.usageMapper.toModel(usageRequestDto);
        UsageModel usageModelSaved = this.usageUseCase.update(id, equipmentLocationModel);
        return new ResponseEntity<>(this.usageMapper.toResponse(usageModelSaved), HttpStatus.OK);
    }

    @GetMapping("/getAllByName/{name}")
    public ResponseEntity<List<UsageResponseDto>> getAllByName(@PathVariable("name") String name){
        return new ResponseEntity<>(this.usageUseCase.getAllByName(name).stream().map(this.usageMapper::toResponse).toList(), HttpStatus.OK);
    }



    @GetMapping("/getAll")
    public ResponseEntity<List<UsageResponseDto>> getAll(){
       try{
           return new ResponseEntity<>(this.usageUseCase.getAll().stream().map(this.usageMapper::toResponse).toList(), HttpStatus.OK);
       } catch (Exception e) {
           e.printStackTrace();
           throw new RuntimeException(e);
       }
    }

    @GetMapping("/getAllPage")
    public ResponseEntity<Page<UsageResponseDto>> getAllPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int elements
    ){
        Pageable pageable = PageRequest.of(page, elements, Sort.by("createAt").descending() );
        return new ResponseEntity<>(this.usageUseCase.getAll(pageable).map(this.usageMapper::toResponse), HttpStatus.OK);
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<UsageResponseDto> getById(@PathVariable("id") Long id){
        return new ResponseEntity<>(this.usageMapper.toResponse(this.usageUseCase.getById(id)), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id){
        this.usageUseCase.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
