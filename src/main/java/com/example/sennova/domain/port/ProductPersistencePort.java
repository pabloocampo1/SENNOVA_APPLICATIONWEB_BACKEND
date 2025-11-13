package com.example.sennova.domain.port;

import com.example.sennova.application.dto.productDtos.ProductRequestDto;
import com.example.sennova.domain.model.ProductModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductPersistencePort {
    Page<ProductModel> findAll(Pageable pageable);
    ProductModel findById(Long id);
    List<ProductModel> findByName(String name);
    List<ProductModel> all();
    void deleteById(Long id);
    ProductModel update(ProductModel productModel, Long id);
    ProductModel save(ProductModel productModel);
}
