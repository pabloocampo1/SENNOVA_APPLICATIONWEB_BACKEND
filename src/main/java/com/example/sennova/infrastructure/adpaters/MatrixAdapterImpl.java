package com.example.sennova.infrastructure.adpaters;

import com.example.sennova.domain.model.MatrixModel;
import com.example.sennova.domain.port.MatrixPersistencePort;
import com.example.sennova.infrastructure.mapperDbo.MatrixMapperDbo;
import com.example.sennova.infrastructure.persistence.entities.Analisys.MatrixEntity;
import com.example.sennova.infrastructure.persistence.repositoryJpa.MatrixRepositoryJpa;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MatrixAdapterImpl implements MatrixPersistencePort {

    private final MatrixRepositoryJpa matrixRepositoryJpa;
    private final MatrixMapperDbo matrixMapperDbo;

    public MatrixAdapterImpl(MatrixRepositoryJpa matrixRepositoryJpa, MatrixMapperDbo matrixMapperDbo) {
        this.matrixRepositoryJpa = matrixRepositoryJpa;
        this.matrixMapperDbo = matrixMapperDbo;
    }


    @Override
    public MatrixModel save(MatrixModel matrixModel) {
        return this.matrixMapperDbo.matrixToModel(
                this.matrixRepositoryJpa.save(
                        this.matrixMapperDbo.matrixToEntity(matrixModel)));
    }

    @Override
    public List<MatrixModel> findAllByMatrixIdAndAvailableTrue(List<Long> ids) {
        List<MatrixEntity> matrixEntities = this.matrixRepositoryJpa.findAllByMatrixIdInAndAvailableTrue(ids);
        return matrixEntities.stream().map(this.matrixMapperDbo::matrixToModel).toList();
    }

    @Override
    public List<MatrixModel> findAll() {
        List<MatrixEntity> matrixEntities = this.matrixRepositoryJpa.findAll();
        return  matrixEntities.stream().map(this.matrixMapperDbo::matrixToModel).toList();
    }

    @Override
    public List<MatrixModel> findAllAvailable() {
        List<MatrixEntity> matrixEntities = this.matrixRepositoryJpa.findAllByAvailableTrue();
        return matrixEntities.stream().map(this.matrixMapperDbo::matrixToModel).toList();
    }
}
