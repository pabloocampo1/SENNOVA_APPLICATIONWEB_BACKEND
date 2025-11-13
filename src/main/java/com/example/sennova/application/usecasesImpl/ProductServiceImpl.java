package com.example.sennova.application.usecasesImpl;

import com.example.sennova.application.dto.productDtos.ProductRequestDto;
import com.example.sennova.application.dto.productDtos.ProductResponseBasicDto;
import com.example.sennova.application.mapper.ProductMapper;
import com.example.sennova.application.usecases.ProductUseCase;
import com.example.sennova.domain.model.ProductModel;
import com.example.sennova.domain.port.ProductPersistencePort;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ProductServiceImpl implements ProductUseCase {

    private final ProductPersistencePort productPersistencePort;


    @Autowired
    public ProductServiceImpl(ProductPersistencePort productPersistencePort) {
        this.productPersistencePort = productPersistencePort;
    }

    @Override
    public Page<ProductModel> getAll(Pageable pageable) {
        return this.productPersistencePort.findAll(pageable);
    }

    @Override
    public List<ProductModel> all() {
        return this.productPersistencePort.all();
    }

    @Override
    public ProductModel getById(@Valid Long id) {
        return this.productPersistencePort.findById(id);
    }

    @Override
    public List<ProductModel> getByName(@Valid String name) {
        return this.productPersistencePort.findByName(name);
    }

    @Override
    @Transactional
    public void deleteProduct(@Valid Long id) {
        try{
            this.productPersistencePort.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ProductModel editProduct(@Valid ProductModel productModel, @Valid Long id) {
        if (productModel.getProductId() == null){
            throw new IllegalArgumentException("No se pudo realizar la edicion del usuario, intentalo mas tarde.");
        }
        return this.productPersistencePort.update(productModel, id);
    }

    @Override
    @Transactional
    public ProductModel save(@Valid ProductModel productModel) {
        return this.productPersistencePort.save(productModel);
    }
}


