package com.example.sennova.infrastructure.adpaters;

import com.example.sennova.domain.model.AnalysisModel;
import com.example.sennova.domain.port.ProductPersistencePort;
import com.example.sennova.infrastructure.mapperDbo.ProductMapperDbo;
import com.example.sennova.infrastructure.persistence.entities.Analisys.AnalysisEntity;
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
    public Page<AnalysisModel> findAll(Pageable pageable) {
         Page<AnalysisEntity> productEntityPage = this.productRepositoryJpa.findAll(pageable);
        return productEntityPage.map(this.productMapperDbo::toModel);
    }

    @Override
    public AnalysisModel findById(Long id) {
         AnalysisEntity analysisEntity = this.productRepositoryJpa.findById(id)
                 .orElseThrow(() -> new UsernameNotFoundException("No se encontro el producto"));

        return this.productMapperDbo.toModel(analysisEntity);
    }

    @Override
    public List<AnalysisModel> findByName(String name) {
        List<AnalysisEntity> productEntities = this.productRepositoryJpa.findAllByAnalysisNameContainingIgnoreCase(name);

        return productEntities.stream().map(this.productMapperDbo::toModel).toList();
    }

    @Override
    public List<AnalysisModel> all() {
         List<AnalysisEntity> productEntities1prueba = this.productRepositoryJpa.findAll();
         List<AnalysisEntity> productEntitiesPruebaCompletedAndSave = productEntities1prueba.stream().map(productEntity -> {
             productEntity.setAvailable(true);
            return this.productRepositoryJpa.save(productEntity);

         }).toList();

         List<AnalysisEntity> productEntities = this.productRepositoryJpa.findAllByAvailableTrue();
         List<AnalysisModel> analysisModelList = productEntities.stream().map(this.productMapperDbo::toModel).toList();
        return analysisModelList;
    }

    @Override
    public void deleteById(Long id) {
         if (!this.productRepositoryJpa.existsById(id)){
             throw new UsernameNotFoundException("no se encontro el usuario");
         }

         this.productRepositoryJpa.deleteById(id);

    }

    @Override
    public AnalysisModel update(AnalysisModel analysisModel, Long id) {
        if (!this.productRepositoryJpa.existsById(id)){
            throw new UsernameNotFoundException("no se encontro el usuario");
        }
         AnalysisEntity analysisEntity = this.productMapperDbo.toEntity(analysisModel);
        AnalysisEntity productSaved = this.productRepositoryJpa.save(analysisEntity);
        return this.productMapperDbo.toModel(productSaved);
    }

    @Override
    public AnalysisModel save(AnalysisModel analysisModel) {
        AnalysisEntity analysisEntity = this.productMapperDbo.toEntity(analysisModel);
        AnalysisEntity productSaved = this.productRepositoryJpa.save(analysisEntity);
        return this.productMapperDbo.toModel(productSaved);
    }

    @Override
    public List<AnalysisModel> findAnalysisByMatrix(Long matrixId) {
         List<AnalysisEntity> entities = this.productRepositoryJpa.findAnalysisByMatrix(matrixId);
        return entities.stream().map(this.productMapperDbo::toModel).toList();
    }

    @Override
    public List<AnalysisModel> findAllAnalysisByUser(Long userId) {
         List<AnalysisEntity> entities = this.productRepositoryJpa.findAllAnalysisByUser(userId);
        return entities.stream().map(this.productMapperDbo::toModel).toList();
    }

    @Override
    public void saveAll(List<AnalysisModel> analysisModels) {
        this.productRepositoryJpa.saveAll(analysisModels.stream().map(this.productMapperDbo::toEntity).toList());
    }
}
