package com.example.sennova.infrastructure.adpaters;

import com.example.sennova.application.dto.testeRequest.CustomerResponse;
import com.example.sennova.domain.model.UserModel;
import com.example.sennova.domain.model.testRequest.CustomerModel;
import com.example.sennova.domain.model.testRequest.TestRequestModel;
import com.example.sennova.domain.port.TestRequestPersistencePort;
import com.example.sennova.infrastructure.mapperDbo.TestRequestMapperDbo;
import com.example.sennova.infrastructure.mapperDbo.UserMapperDbo;
import com.example.sennova.infrastructure.persistence.entities.UserEntity;
import com.example.sennova.infrastructure.persistence.entities.analysisRequestsEntities.TestRequestEntity;
import com.example.sennova.infrastructure.persistence.repositoryJpa.TestRequestRepositoryJpa;
import com.example.sennova.infrastructure.projection.SampleInfoSummaryTestRequestProjection;
import org.antlr.v4.runtime.ListTokenSource;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class TestRequestAdapterImpl implements TestRequestPersistencePort {

    private final TestRequestRepositoryJpa testRequestRepositoryJpa;
    private final TestRequestMapperDbo testRequestMapperDbo;
    private final UserMapperDbo userMapperDbo;

    public TestRequestAdapterImpl(TestRequestRepositoryJpa testRequestRepositoryJpa, TestRequestMapperDbo testRequestMapperDbo, UserMapperDbo userMapperDbo) {
        this.testRequestRepositoryJpa = testRequestRepositoryJpa;
        this.testRequestMapperDbo = testRequestMapperDbo;
        this.userMapperDbo = userMapperDbo;
    }

    @Override
    public TestRequestModel save(TestRequestModel testRequestModel) {
        TestRequestEntity testRequestEntity = this.testRequestRepositoryJpa.save(this.testRequestMapperDbo.toEntity(testRequestModel));
        return this.testRequestMapperDbo.toModel(testRequestEntity);
    }

    @Override
    public List<TestRequestModel> getAll() {
        List<TestRequestEntity> testRequestEntities = this.testRequestRepositoryJpa.findAll();
        return testRequestEntities.stream().map(this.testRequestMapperDbo::toModel).toList();
    }

    @Override
    public Boolean existsById(Long testRequestId) {
        return this.testRequestRepositoryJpa.existsById(testRequestId);
    }

    @Override
    public List<SampleInfoSummaryTestRequestProjection> findSamplesInfoByTestRequest(Long testRequestId) {
        return this.testRequestRepositoryJpa.findSamplesByTestRequest(testRequestId);
    }

    @Override
    public void deleteById(Long testRequestId) {
        this.testRequestRepositoryJpa.deleteById(testRequestId);
    }

    @Override
    public Optional<TestRequestModel> findById(Long id) {
        Optional<TestRequestEntity> testRequestEntity =  this.testRequestRepositoryJpa.findById(id);

        return Optional.of(this.testRequestMapperDbo.toModel(testRequestEntity.get()));
    }

    @Override
    public List<TestRequestModel> findAllByState(String state) {
        List<TestRequestEntity> entities = this.testRequestRepositoryJpa.findAllByState(state);
        return entities.stream().map(this.testRequestMapperDbo::toModel).toList();
    }

    @Override
    public List<TestRequestModel> findAllByCustomerName(String customerName) {
        List<TestRequestEntity> testRequestEntities = this.testRequestRepositoryJpa.findAllByCustomer_CustomerNameContainingIgnoreCase(customerName);
        System.out.println("por cliente");
        System.out.println(testRequestEntities);
        return testRequestEntities.stream().map(this.testRequestMapperDbo::toModel).toList();
    }

    @Override
    public List<TestRequestModel> findAllByRequestCode(String requestCode) {
        List<TestRequestEntity> testRequestEntities = this.testRequestRepositoryJpa.findAllByRequestCodeContainingIgnoreCase(requestCode);
        System.out.println("por code");
        System.out.println(testRequestEntities);
        return testRequestEntities.stream().map(this.testRequestMapperDbo::toModel).toList();
    }

    @Override
    public List<TestRequestModel> findAllTestRequestAccepted() {
        List<TestRequestEntity> entities = this.testRequestRepositoryJpa.findAllByIsApprovedTrue();
        return entities.stream().map(this.testRequestMapperDbo::toModel).toList();
    }

    @Override
    public List<TestRequestModel> findAllByDeliveryState(String state) {
        List<TestRequestEntity> testRequestEntities = this.testRequestRepositoryJpa.findAllByDeliveryStatusContainingIgnoreCase(state);
        return testRequestEntities.stream().map(this.testRequestMapperDbo::toModel).toList();
    }

    @Override
    public List<TestRequestModel> findAllTestRequestByRequestCode(String code) {
        List<TestRequestEntity> testRequestEntities = this.testRequestRepositoryJpa.findAllByRequestCodeContainingIgnoreCase(code);
        return testRequestEntities.stream().map(this.testRequestMapperDbo::toModel).toList();
    }

    @Override
    public void assignResponsible(TestRequestModel testRequestModel, List<UserModel> users) {
        TestRequestEntity testRequestEntity = this.testRequestMapperDbo.toEntity(testRequestModel);
        List<UserEntity> userEntity = users.stream().map(this.userMapperDbo::toEntity).toList();
        
        testRequestEntity.setMembers(userEntity);

        this.testRequestRepositoryJpa.save(testRequestEntity);
    }




}
