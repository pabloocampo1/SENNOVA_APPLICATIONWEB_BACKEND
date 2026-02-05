package com.example.sennova.application.usecasesImpl;

import com.example.sennova.application.dto.UserDtos.UserListResponse;
import com.example.sennova.application.dto.productDtos.AnalysisAssignMatrixRequest;
import com.example.sennova.application.dto.productDtos.AnalysisAssignedQualifiedUsers;
import com.example.sennova.application.dto.productDtos.AnalysisInfoDtoResponse;
import com.example.sennova.application.mapper.UserMapper;
import com.example.sennova.application.usecases.ProductUseCase;
import com.example.sennova.application.usecases.UserUseCase;
import com.example.sennova.domain.model.AnalysisModel;
import com.example.sennova.domain.model.MatrixModel;
import com.example.sennova.domain.model.UserModel;
import com.example.sennova.domain.port.MatrixPersistencePort;
import com.example.sennova.domain.port.ProductPersistencePort;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Service
public class ProductServiceImpl implements ProductUseCase {

    private final ProductPersistencePort productPersistencePort;
    private final MatrixPersistencePort matrixPersistencePort;
    private final UserUseCase userUseCase;
    private final UserMapper userMapper;


    @Autowired
    public ProductServiceImpl(ProductPersistencePort productPersistencePort, MatrixPersistencePort matrixPersistencePort, UserUseCase userUseCase, UserMapper userMapper) {
        this.productPersistencePort = productPersistencePort;
        this.matrixPersistencePort = matrixPersistencePort;
        this.userUseCase = userUseCase;
        this.userMapper = userMapper;
    }

    @Override
    public Page<AnalysisModel> getAll(Pageable pageable) {
        return this.productPersistencePort.findAll(pageable);
    }

    @Override
    public List<AnalysisModel> all() {
        return this.productPersistencePort.all();
    }

    @Override
    public AnalysisModel getById(@Valid Long id) {
        return this.productPersistencePort.findById(id);
    }

    @Override
    public List<AnalysisModel> getByName(@Valid String name) {
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
    @Transactional
    public AnalysisModel editProduct(@Valid AnalysisModel analysisModel, @Valid Long id) {
        if (analysisModel.getAnalysisId() == null){
            throw new IllegalArgumentException("No se pudo realizar la edicion del usuario, intentalo mas tarde.");
        }
        return this.productPersistencePort.update(analysisModel, id);
    }

    @Override
    @Transactional
    public AnalysisModel save(@Valid AnalysisModel analysisModel) {
        return this.productPersistencePort.save(analysisModel);
    }

    @Override
    public AnalysisInfoDtoResponse getAllInfoById(Long id) {
         AnalysisModel analysisModel = this.getById(id);
        List<UserListResponse> usersDtoResponse = this.userMapper.toResponseUserList(analysisModel.getQualifiedUsers());


        return AnalysisInfoDtoResponse.fromModel(analysisModel, usersDtoResponse);
    }

    @Override
    public AnalysisInfoDtoResponse assignQualifiedUsers(AnalysisAssignedQualifiedUsers dto) {
        //  search the users  and analisis
        AnalysisModel analysisModel = this.getById(dto.analysisId());
        List<UserModel> users = this.userUseCase.findAllById(dto.users());

        // assigned
        List<UserModel> usersAssigned = analysisModel.getQualifiedUsers() != null
                ? analysisModel.getQualifiedUsers()
                :  new ArrayList<>();

        for (UserModel newUser : users){

            boolean alreadyExist = analysisModel.getQualifiedUsers().stream().anyMatch(u -> u.getUserId().equals(newUser.getUserId()));
            if(!alreadyExist){
                  usersAssigned.add(newUser);
            }
        }

        analysisModel.setQualifiedUsers(usersAssigned);

        // save the relation
        AnalysisModel savedModel = this.productPersistencePort.save(analysisModel);

        // map user to dto response
        List<UserListResponse> usersDtoResponse = this.userMapper.toResponseUserList(usersAssigned);


        return AnalysisInfoDtoResponse.fromModel(savedModel, usersDtoResponse);
    }

    @Override
    @Transactional
    public AnalysisInfoDtoResponse removeUser(Long userId, Long analysisId) {
        AnalysisModel analysisModel = this.getById(analysisId);

        List<UserModel> usersAssignedUpdate = analysisModel.getQualifiedUsers()
                .stream()
                .filter(userModel -> !userModel.getUserId().equals(userId))
                .toList();

        analysisModel.setQualifiedUsers(usersAssignedUpdate);

        AnalysisModel analysisModelSaved = this.productPersistencePort.save(analysisModel);

        List<UserListResponse> usersDtoResponse = this.userMapper.toResponseUserList(usersAssignedUpdate);


        return AnalysisInfoDtoResponse.fromModel(analysisModelSaved, usersDtoResponse);
    }

    @Override
    @Transactional
    public AnalysisInfoDtoResponse assignMatrices(AnalysisAssignMatrixRequest dto) {
        // get the data entities
        AnalysisModel analysisModel = this.getById(dto.analysisId());
        List<MatrixModel> matrices = this.matrixPersistencePort.findAllByMatrixIdAndAvailableTrue(dto.matrices());

        System.out.println(matrices);

        List<MatrixModel> matricesAssigned = analysisModel.getMatrices() != null
                ? analysisModel.getMatrices()
                : new ArrayList<>();

        for (MatrixModel matrix : matrices){
              boolean isExist = analysisModel.getMatrices().stream().anyMatch(m -> m.getMatrixId().equals(matrix.getMatrixId()));

              if(!isExist){
                  matricesAssigned.add(matrix);


                  if (matrix.getAnalysis() == null) {
                      matrix.setAnalysis(new ArrayList<>());
                  }
                  matrix.getAnalysis().add(analysisModel);
              }
        }


        analysisModel.setMatrices(matricesAssigned);
        AnalysisModel analysisModelSaved = this.productPersistencePort.save(analysisModel);

        List<UserListResponse> usersDtoResponse = this.userMapper.toResponseUserList(analysisModelSaved.getQualifiedUsers());



        return AnalysisInfoDtoResponse.fromModel(analysisModelSaved, usersDtoResponse);
    }

    @Override
    @Transactional
    public AnalysisInfoDtoResponse removeMatrix(Long analysisId, Long matrixId) {
        AnalysisModel analysisModel = this.getById(analysisId);

        List<MatrixModel> listUpdate = analysisModel.getMatrices().stream()
                        .filter(m -> !m.getMatrixId().equals(matrixId))
                                .toList();

        analysisModel.setMatrices(listUpdate);

        AnalysisModel analysisModelSaved = this.productPersistencePort.save(analysisModel);

        List<UserListResponse> usersDtoResponse = this.userMapper.toResponseUserList(analysisModelSaved.getQualifiedUsers());


        return AnalysisInfoDtoResponse.fromModel(analysisModelSaved, usersDtoResponse);
    }

    @Override
    public List<AnalysisModel> getAnalysisByMatrix(Long matrix) {
        return this.productPersistencePort.findAnalysisByMatrix(matrix);
    }

    @Override
    public MatrixModel saveMatrix(MatrixModel m) {
        return this.matrixPersistencePort.save(m);
    }

    @Override
    public List<MatrixModel> getAllMatrix() {
        return this.matrixPersistencePort.findAll();
    }

    @Override
    public List<MatrixModel> getAllMatrixAvailable() {
        return this.matrixPersistencePort.findAllAvailable();
    }
}


