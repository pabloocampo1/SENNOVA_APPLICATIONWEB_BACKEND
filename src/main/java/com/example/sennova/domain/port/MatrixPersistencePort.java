package com.example.sennova.domain.port;

import com.example.sennova.domain.model.MatrixModel;

import java.util.List;

public interface MatrixPersistencePort {
    
    MatrixModel save(MatrixModel matrixModel);
    List<MatrixModel> findAllByMatrixIdAndAvailableTrue(List<Long> ids);
    List<MatrixModel> findAll();
    List<MatrixModel> findAllAvailable();

    
}
