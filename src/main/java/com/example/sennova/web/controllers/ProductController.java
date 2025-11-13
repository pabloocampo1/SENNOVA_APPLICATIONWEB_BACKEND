package com.example.sennova.web.controllers;

import com.example.sennova.application.dto.productDtos.ProductRequestDto;
import com.example.sennova.application.dto.productDtos.ProductResponseBasicDto;
import com.example.sennova.application.mapper.ProductMapper;
import com.example.sennova.application.usecases.ProductUseCase;
import com.example.sennova.domain.model.ProductModel;
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
@RequestMapping("/api/v1/product")
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
        Page<ProductModel> productModelList = this.productUseCase.getAll(pageable);
        Page<ProductResponseBasicDto> productResponseBasicDtoPage = productModelList.map(this.productMapper::toResponse);
        return new ResponseEntity<>(productResponseBasicDtoPage, HttpStatus.OK);
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<ProductResponseBasicDto> getById(@PathVariable("id") Long id) {
        return new ResponseEntity<>(this.productMapper.toResponse(this.productUseCase.getById(id)), HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<ProductResponseBasicDto>> all(){
        List<ProductModel> modelList = this.productUseCase.all();
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
        ProductModel productModel = this.productMapper.toModel(productRequestDto);
        return new ResponseEntity<>(this.productMapper.toResponse(this.productUseCase.save(productModel)), HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ProductResponseBasicDto> update(@RequestBody @Valid ProductRequestDto productRequestDto, @PathVariable("id") Long id) {
        ProductModel productModel = this.productMapper.toModel(productRequestDto);
        return new ResponseEntity<>(this.productMapper.toResponse(this.productUseCase.editProduct(productModel, id)), HttpStatus.OK);
    }
}
