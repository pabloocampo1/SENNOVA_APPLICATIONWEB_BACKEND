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
import com.example.sennova.web.exception.EntityNotFoundException;
import org.antlr.v4.runtime.ListTokenSource;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public TestRequestEntity saveEntity(TestRequestEntity testRequestEntity) {
        return this.testRequestRepositoryJpa.save(testRequestEntity);
    }

    @Override
    public Page<TestRequestModel> findAllPage(Pageable pageable) {
        return this.testRequestRepositoryJpa.findAll(pageable).map(this.testRequestMapperDbo::toModel);
    }

    @Override
    public List<TestRequestModel> getAll() {
        List<TestRequestEntity> testRequestEntities = this.testRequestRepositoryJpa.findAllByOrderByCreateAtDesc();
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
    public Optional<TestRequestModel> findByRequestCode(String requestCode) {
        Optional<TestRequestEntity> testRequestEntity = this.testRequestRepositoryJpa.findByRequestCode(requestCode);
        return testRequestEntity
                .map(this.testRequestMapperDbo::toModel);
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
    public List<TestRequestModel> findAllByDueDate(LocalDate today) {
        List<TestRequestEntity> testRequestList = this.testRequestRepositoryJpa.findAllByDueDate(today);
        return testRequestList.stream().map(this.testRequestMapperDbo::toModel).toList();
    }

    @Override
    public List<TestRequestModel> findAllTestRequestAccepted() {
        List<TestRequestEntity> entities = this.testRequestRepositoryJpa.findAllByIsApprovedTrueOrderByCreateAtDesc();
        return entities.stream().map(this.testRequestMapperDbo::toModel).toList();
    }

    @Override
    public Page<TestRequestModel> findAllTestRequestAccepted(Pageable pageable) {
        Page<TestRequestEntity> entities = this.testRequestRepositoryJpa.findAllByIsApprovedTrueOrderByCreateAtDesc(pageable);
        return entities.map(this.testRequestMapperDbo::toModel);
    }

    @Override
    public long countNotDeliveredSamples(Long testRequestId) {
        return this.testRequestRepositoryJpa.countNotDeliveredSamples(testRequestId);
    }

    @Override
    public List<TestRequestModel> findAllByDueDateExpired(LocalDate today) {
        List<TestRequestEntity> entities = this.testRequestRepositoryJpa.findAllByDueDateBefore(today)  ;
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
    public void assignResponsible(Long testRequestId, List<UserModel> users) {
        TestRequestEntity testRequestEntity = this.testRequestRepositoryJpa.findById(testRequestId)
                .orElseThrow(() -> new EntityNotFoundException("No se encontro el ensayo con id : " + testRequestId));

        List<UserEntity> userAssignedBefore = testRequestEntity.getMembers();
        List<UserEntity> usersToAdd = users.stream().map(this.userMapperDbo::toEntity).toList();

        List<UserEntity> uniqueUsersToAdd = usersToAdd.stream()
                .filter(newUser -> userAssignedBefore.stream()
                        .noneMatch(existingUser -> existingUser.getUserId().equals(newUser.getUserId())))
                .toList();

        List<UserEntity> updatedMembers = new ArrayList<>(userAssignedBefore);
        updatedMembers.addAll(uniqueUsersToAdd);


      testRequestEntity.setMembers(updatedMembers);

        this.testRequestRepositoryJpa.save(testRequestEntity);
    }

    @Override
    public Optional<TestRequestEntity> getWithSamplesAndAnalysis(String requestCode) {
        return this.testRequestRepositoryJpa.getWithSamplesAndAnalysis(requestCode);
    }

    @Override
    public void removeMember(Long userId, Long testRequestId) {
           TestRequestEntity testRequestEntity = this.testRequestRepositoryJpa.findById(testRequestId)
                   .orElseThrow(() -> new EntityNotFoundException("No se encontro el ensayo con id  :" + testRequestId));


        testRequestEntity.setMembers(
                testRequestEntity.getMembers()
                        .stream()
                        .filter(user -> !user.getUserId().equals(userId))
                        .collect(Collectors.toList())
        );



        this.testRequestRepositoryJpa.save(testRequestEntity);
    }

    @Override
    public List<TestRequestEntity> findAllByYear(String year) {

        if (year == null || year.isEmpty()) {
            int currentYear = LocalDate.now().getYear();
            year = String.valueOf(currentYear);
        }


       return this.testRequestRepositoryJpa.findAllByYear(year);
    }


}
