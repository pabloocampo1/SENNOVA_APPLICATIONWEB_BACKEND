package com.example.sennova.web.controllers;

import com.example.sennova.application.dto.inventory.LocationResponseDto;
import com.example.sennova.application.dto.inventory.LocationRequestDto;
import com.example.sennova.application.mapper.LocationMapper;
import com.example.sennova.application.usecases.LocationUseCase;
import com.example.sennova.domain.model.LocationModel;
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
@RequestMapping("/api/v1/location")
public class LocationController {
    private final LocationUseCase locationUseCase;
    private final LocationMapper locationMapper;

    @Autowired
    public LocationController(LocationUseCase locationUseCase, LocationMapper locationMapper) {
        this.locationUseCase = locationUseCase;
        this.locationMapper = locationMapper;
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<LocationResponseDto> getById(@PathVariable("id") @Valid Long id){
        return new ResponseEntity<>(this.locationMapper.toResponse(this.locationUseCase.getById(id)),HttpStatus.OK );
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<LocationResponseDto>> getAll(){
        List<LocationModel> all = this.locationUseCase.getAll();
        return new ResponseEntity<>(all.stream().map(this.locationMapper::toResponse).toList(),HttpStatus.OK );
    }

    @GetMapping("/getByName/{name}")
    public ResponseEntity<List<LocationResponseDto>> getById(@PathVariable("name") @Valid String name){
        return new ResponseEntity<>(
                this.locationUseCase.getAllByName(name).stream().map(this.locationMapper::toResponse).toList(),
                HttpStatus.OK );
    }


    @PostMapping("/save")
    public ResponseEntity<LocationResponseDto> save(@RequestBody @Valid LocationRequestDto locationRequestDto) {
        LocationModel locationModel = this.locationMapper.toModel(locationRequestDto);
        LocationModel locationModelSaved = this.locationUseCase.save(locationModel);
        return new ResponseEntity<>(this.locationMapper.toResponse(locationModelSaved), HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<LocationResponseDto> update(
            @RequestBody @Valid LocationRequestDto locationRequestDto,
            @PathVariable("id") @Valid Long id) {
        LocationModel locationModel = this.locationMapper.toModel(locationRequestDto);
        LocationModel locationModelSaved = this.locationUseCase.update( id, locationModel);
        return new ResponseEntity<>(this.locationMapper.toResponse(locationModelSaved), HttpStatus.OK);
    }

    @GetMapping("/getAllPage")
    public ResponseEntity<Page<LocationResponseDto>> getAll(
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "15")  int elements
    ){
        Pageable pageable = PageRequest.of(page, elements, Sort.by("createAt").descending());
        Page<LocationModel> locationModelPage = this.locationUseCase.getAllPage(pageable);

        Page<LocationResponseDto> pageToReturn = locationModelPage.map(this.locationMapper::toResponse);
        return new ResponseEntity<>(pageToReturn,HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id){
        this.locationUseCase.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
