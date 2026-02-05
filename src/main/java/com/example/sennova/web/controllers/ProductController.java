package com.example.sennova.web.controllers;

import com.example.sennova.application.dto.productDtos.*;
import com.example.sennova.application.mapper.ProductMapper;
import com.example.sennova.application.usecases.ProductUseCase;
import com.example.sennova.domain.model.AnalysisModel;
import com.example.sennova.domain.model.MatrixModel;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/analysis")
public class ProductController {

    private final ProductUseCase productUseCase;
    private final ProductMapper productMapper;

    @Autowired
    public ProductController(ProductUseCase productUseCase, ProductMapper productMapper) {
        this.productUseCase = productUseCase;
        this.productMapper = productMapper;
    }

    @GetMapping("/getAll")
    public ResponseEntity<Page<ProductResponseBasicDto>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "9") int elements
    ) {

        Pageable pageable = PageRequest.of(page, elements, Sort.by("createAt").descending());
        Page<AnalysisModel> productModelList = this.productUseCase.getAll(pageable);
        Page<ProductResponseBasicDto> productResponseBasicDtoPage = productModelList.map(this.productMapper::toResponse);
        return new ResponseEntity<>(productResponseBasicDtoPage, HttpStatus.OK);
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<ProductResponseBasicDto> getById(@PathVariable("id") Long id) {
        return new ResponseEntity<>(this.productMapper.toResponse(this.productUseCase.getById(id)), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AnalysisInfoDtoResponse> getAllInfoById(@PathVariable("id") Long id) {
        return new ResponseEntity<>(this.productUseCase.getAllInfoById(id), HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<ProductResponseBasicDto>> all(){
        List<AnalysisModel> modelList = this.productUseCase.all();
        return new ResponseEntity<>(modelList.stream().map(this.productMapper::toResponse).toList(), HttpStatus.OK);
    }

    @GetMapping("/getByName/{name}")
    public ResponseEntity<List<ProductResponseBasicDto>> getByName(@PathVariable("name") String name) {
        return new ResponseEntity<>(this.productMapper.toResponse(this.productUseCase.getByName(name)), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> getByName(@PathVariable("id") Long id) {
        this.productUseCase.deleteProduct(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<ProductResponseBasicDto> save(@RequestBody @Valid ProductRequestDto productRequestDto) {
        AnalysisModel analysisModel = this.productMapper.toModel(productRequestDto);
        return new ResponseEntity<>(this.productMapper.toResponse(this.productUseCase.save(analysisModel)), HttpStatus.CREATED);
    }

    @PostMapping("/assign/user")
    public ResponseEntity<AnalysisInfoDtoResponse> assignUsers(@RequestBody @Valid AnalysisAssignedQualifiedUsers usersId) {

        return new ResponseEntity<>(this.productUseCase.assignQualifiedUsers(usersId), HttpStatus.CREATED);
    }

    @PostMapping("/assign/matrix")
    public ResponseEntity<AnalysisInfoDtoResponse> assignMatrix(@RequestBody @Valid AnalysisAssignMatrixRequest matrices) {

        return new ResponseEntity<>(this.productUseCase.assignMatrices(matrices), HttpStatus.CREATED);
    }

    @PostMapping("/remove/matrix/{matrixId}/{analysisId}")
    public ResponseEntity<AnalysisInfoDtoResponse> removeMatrix(@PathVariable("matrixId") @Valid Long matrixId,
                                                                @PathVariable("analysisId") @Valid Long analysisId) {

        return new ResponseEntity<>(this.productUseCase.removeMatrix(analysisId, matrixId), HttpStatus.CREATED);
    }

    @PostMapping("/remove/user/{userId}/{analysisId}")
    public ResponseEntity<AnalysisInfoDtoResponse> removeUser(@PathVariable("userId") @Valid Long userId,
                                                              @PathVariable("analysisId") @Valid Long analysisId) {

        return new ResponseEntity<>(this.productUseCase.removeUser(userId, analysisId), HttpStatus.CREATED);
    }

    @PostMapping("/matrix")
    public ResponseEntity<MatrixModel> saveMatrix(@RequestBody MatrixModel m) {
        System.out.println(m.getMatrixName());
        return  new ResponseEntity<>(this.productUseCase.saveMatrix(m), HttpStatus.CREATED);
    }

    @GetMapping("/matrix/get-all")
    public ResponseEntity<List<MatrixModel>> getAllMatrix(){
       return new ResponseEntity<>(this.productUseCase.getAllMatrix(), HttpStatus.OK );
    }

    @GetMapping("/matrix/get-all/available")
    public ResponseEntity<List<MatrixModel>> getAllMatrixAvailable(){
        return new ResponseEntity<>(this.productUseCase.getAllMatrixAvailable(), HttpStatus.OK );
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ProductResponseBasicDto> update(@RequestBody @Valid ProductRequestDto productRequestDto, @PathVariable("id") Long id) {
        AnalysisModel analysisModel = this.productMapper.toModel(productRequestDto);
        return new ResponseEntity<>(this.productMapper.toResponse(this.productUseCase.editProduct(analysisModel, id)), HttpStatus.OK);
    }

        @GetMapping("/all-by-matrix/{matrixId}")
    public ResponseEntity<List<ProductResponseBasicDto>> getAnalysisBuMatrix(@PathVariable("matrixId") Long matrixId){

       return new ResponseEntity<>(this.productUseCase.getAnalysisByMatrix(matrixId).stream().map(this.productMapper::toResponse).toList(), HttpStatus.OK);
    }
}
