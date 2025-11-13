package com.example.sennova.application.usecases;

import com.example.sennova.application.dto.productDtos.ProductRequestDto;
import com.example.sennova.application.dto.productDtos.ProductResponseBasicDto;
import com.example.sennova.domain.model.ProductModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductUseCase {
    Page<ProductModel> getAll(Pageable pageable);
    List<ProductModel> all();
    ProductModel getById(Long id);
    List<ProductModel> getByName(String name);
    void deleteProduct(Long id);
    ProductModel editProduct(ProductModel productModel, Long id);
    ProductModel save(ProductModel productModel);
}
