package com.example.sennova.infrastructure.adpaters;

import com.example.sennova.application.dto.productDtos.ProductRequestDto;
import com.example.sennova.domain.model.ProductModel;
import com.example.sennova.domain.port.ProductPersistencePort;
import com.example.sennova.infrastructure.mapperDbo.ProductMapperDbo;
import com.example.sennova.infrastructure.persistence.entities.analysisRequestsEntities.ProductEntity;
import com.example.sennova.infrastructure.persistence.repositoryJpa.ProductRepositoryJpa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProductAdapterImpl implements ProductPersistencePort {

    private final ProductRepositoryJpa productRepositoryJpa;
    private final ProductMapperDbo productMapperDbo;

     @Autowired
    public ProductAdapterImpl(ProductRepositoryJpa productRepositoryJpa, ProductMapperDbo productMapperDbo) {
        this.productRepositoryJpa = productRepositoryJpa;
         this.productMapperDbo = productMapperDbo;
     }


    @Override
    public Page<ProductModel> findAll(Pageable pageable) {
         Page<ProductEntity> productEntityPage = this.productRepositoryJpa.findAll(pageable);
        return productEntityPage.map(this.productMapperDbo::toModel);
    }

    @Override
    public ProductModel findById(Long id) {
         ProductEntity productEntity = this.productRepositoryJpa.findById(id)
                 .orElseThrow(() -> new UsernameNotFoundException("No se encontro el producto"));

        return this.productMapperDbo.toModel(productEntity);
    }

    @Override
    public List<ProductModel> findByName(String name) {
        List<ProductEntity> productEntities = this.productRepositoryJpa.findAllByAnalysisContainingIgnoreCase(name);

        return productEntities.stream().map(this.productMapperDbo::toModel).toList();
    }

    @Override
    public List<ProductModel> all() {
         List<ProductEntity> productEntities1prueba = this.productRepositoryJpa.findAll();
         List<ProductEntity> productEntitiesPruebaCompletedAndSave = productEntities1prueba.stream().map(productEntity -> {
             productEntity.setAvailable(true);
            return this.productRepositoryJpa.save(productEntity);

         }).toList();

         List<ProductEntity> productEntities = this.productRepositoryJpa.findAllByAvailableTrue();
         List<ProductModel> productModelList = productEntities.stream().map(this.productMapperDbo::toModel).toList();
        return productModelList;
    }

    @Override
    public void deleteById(Long id) {
         if (!this.productRepositoryJpa.existsById(id)){
             throw new UsernameNotFoundException("no se encontro el usuario");
         }

         this.productRepositoryJpa.deleteById(id);

    }

    @Override
    public ProductModel update(ProductModel productModel, Long id) {
        if (!this.productRepositoryJpa.existsById(id)){
            throw new UsernameNotFoundException("no se encontro el usuario");
        }
         ProductEntity productEntity = this.productMapperDbo.toEntity(productModel);
        ProductEntity productSaved = this.productRepositoryJpa.save(productEntity);
        return this.productMapperDbo.toModel(productSaved);
    }

    @Override
    public ProductModel save(ProductModel productModel) {
        ProductEntity productEntity = this.productMapperDbo.toEntity(productModel);
        ProductEntity productSaved = this.productRepositoryJpa.save(productEntity);
        return this.productMapperDbo.toModel(productSaved);
    }
}
