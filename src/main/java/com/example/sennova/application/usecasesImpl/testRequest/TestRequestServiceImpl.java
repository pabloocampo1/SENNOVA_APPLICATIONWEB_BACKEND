package com.example.sennova.application.usecasesImpl.testRequest;

import com.example.sennova.application.dto.UserDtos.UserResponseMembersAssigned;
import com.example.sennova.application.dto.testeRequest.*;
import com.example.sennova.application.dto.testeRequest.quotation.QuotationResponse;
import com.example.sennova.application.dto.testeRequest.quotation.SampleQuotationResponse;
import com.example.sennova.application.dto.testeRequest.sample.SamplesByTestRequestDto;
import com.example.sennova.application.mapper.CustomerMapper;
import com.example.sennova.application.usecases.*;
import com.example.sennova.application.usecases.TestRequest.TestRequestUseCase;
import com.example.sennova.application.usecasesImpl.NotificationsService;
import com.example.sennova.domain.constants.RoleConstantsNotification;
import com.example.sennova.domain.constants.TestRequestConstants;
import com.example.sennova.domain.constants.TypeNotifications;
import com.example.sennova.domain.model.ProductModel;
import com.example.sennova.domain.model.UserModel;
import com.example.sennova.domain.model.testRequest.CustomerModel;
import com.example.sennova.domain.model.testRequest.SampleAnalysisModel;
import com.example.sennova.domain.model.testRequest.SampleModel;
import com.example.sennova.domain.model.testRequest.TestRequestModel;
import com.example.sennova.domain.port.TestRequestPersistencePort;
import com.example.sennova.infrastructure.persistence.entities.Notifications;
import com.example.sennova.infrastructure.restTemplate.TestRequestEmailService;
import com.example.sennova.web.exception.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class TestRequestServiceImpl implements TestRequestUseCase {

    private final CustomerMapper customerMapper;
    private final CustomerUseCase customerUseCase;
    private final SampleUseCase sampleUseCase;
    private final SampleAnalysisUseCase sampleAnalysisUseCase;
    private final ProductUseCase productUseCase;
    private final TestRequestPersistencePort testRequestPersistencePort;
    private final NotificationsService notificationsService;
    private final UserUseCase userUseCase;
    private final TestRequestEmailService testRequestEmailService;

    @Autowired
    public TestRequestServiceImpl(CustomerMapper customerMapper, CustomerUseCase customerUseCase, SampleUseCase sampleUseCase, SampleAnalysisUseCase sampleAnalysisUseCase, ProductUseCase productUseCase, TestRequestPersistencePort testRequestPersistencePort, NotificationsService notificationsService, UserUseCase userUseCase,  TestRequestEmailService testRequestEmailService) {
        this.customerMapper = customerMapper;
        this.customerUseCase = customerUseCase;
        this.sampleUseCase = sampleUseCase;
        this.sampleAnalysisUseCase = sampleAnalysisUseCase;
        this.productUseCase = productUseCase;
        this.testRequestPersistencePort = testRequestPersistencePort;
        this.notificationsService = notificationsService;
        this.userUseCase = userUseCase;

        this.testRequestEmailService = testRequestEmailService;
    }


    @Override
    public List<TestRequestModel> getAllTestRequest() {
        return this.testRequestPersistencePort.getAll();
    }

    @Override
    public Page<QuotationResponse> getAllQuotation(Pageable pageable) {
        Page<TestRequestModel> testRequestModelPage = this.testRequestPersistencePort.findAllPage(pageable);
        List<QuotationResponse> content =
                testRequestModelPage.getContent()
                .stream().map(t -> {

                    List<SampleQuotationResponse> samples = t.getSamples()
                            .stream()
                            .map(s -> new SampleQuotationResponse(s.getMatrix(), s.getAnalysisEntities().size()))
                            .toList();

                    QuotationResponse quotationResponse =new QuotationResponse();
                    quotationResponse.setTestRequestId(t.getTestRequestId());
                    quotationResponse.setRequestCode(t.getRequestCode());
                    quotationResponse.setState(t.getState());
                    quotationResponse.setCustomer(t.getCustomer());
                    quotationResponse.setPrice(t.getPrice());
                    quotationResponse.setApprovalDate(t.getApprovalDate());
                    quotationResponse.setDiscardDate(t.getDiscardDate());
                    quotationResponse.setCreateAt(t.getCreateAt());
                    quotationResponse.setIsApproved(t.getIsApproved());
                    quotationResponse.setSamples(samples);

                    return quotationResponse;
                        }).toList();
        return new PageImpl<>(
                content,
                pageable,
                testRequestModelPage.getTotalElements()
                );
    }


    @Override
    public List<TestRequestModel> getAllTestRequestAccepted() {
        return this.testRequestPersistencePort.findAllTestRequestAccepted();
    }

    @Override
    public void updateStatus(TestRequestModel testRequestModel) {
        this.testRequestPersistencePort.save(testRequestModel);
    }

    @Override
    @Transactional
    public TestRequestModel save(TestRequestRecord testRequestRecord) {

        TestRequestModel testRequestModel = new TestRequestModel();
        testRequestModel.setIsApproved(false);


        // create the customer.
        // search the customer, if exist don't save and search and get. if not exist save
        CustomerRequestRecord customerRequestRecord = testRequestRecord.customer();
        CustomerModel customer = this.customerUseCase.save(this.customerMapper.toModel(customerRequestRecord));

        testRequestModel.setCustomer(customer);

        int currentYear = LocalDate.now().getYear();
        int randomNum = (int) (Math.random() * 9000) + 1000;
        String code = currentYear + "-" + randomNum ;

        testRequestModel.setRequestCode(code);


        // create the list of the sample with products.
        // calculte the final price

        AtomicReference<Double> finalPrice = new AtomicReference<>((double) 0);
        AtomicInteger countSampleCode = new AtomicInteger(1)  ;

        TestRequestModel testRequestSaved =  this.testRequestPersistencePort.save(testRequestModel);

        List<SampleModel> samples = new ArrayList<>();
        List<SampleRequestRecord> sampleRequestRecords = testRequestRecord.samples();
        sampleRequestRecords.forEach(sampleRequestRecord -> {
                SampleModel sampleModel = new SampleModel();
                 sampleModel.setMatrix(sampleRequestRecord.matrix());
                 sampleModel.setDescription(sampleRequestRecord.description());
                 sampleModel.setSampleCode("M"+countSampleCode.getAndIncrement() + " - " + randomNum);
                 sampleModel.setTestRequest(testRequestSaved);
                 sampleModel.setStatusReception(false);
                 sampleModel.setIsDelivered(false);

                 // count hoy many anaylis there are for this sample

                sampleModel.setTotalAnalysis(sampleRequestRecord.analysis().size());

                 // save the sample, after create the entity with the products(analysis to do) and sample to save results about that analysis
                SampleModel sampleSaved = this.sampleUseCase.save(sampleModel);



                 // create the entity to result
                 List<ProductQuantityQuote> analysisAndQuantity = sampleRequestRecord.analysis();
                 analysisAndQuantity.forEach(analysis -> {
                     ProductModel product = this.productUseCase.getById(analysis.getProductId());

                     for (int i = 0; i < analysis.getQuantity() ; i++) {
                         SampleAnalysisModel sampleAnalysisModel = new SampleAnalysisModel();
                         sampleAnalysisModel.setStateResult(false);
                         sampleAnalysisModel.setProduct(product);
                         sampleAnalysisModel.setCode("Code-MR/ "+sampleModel.getSampleCode() + "-" + (i+1));
                         sampleAnalysisModel.setSample(sampleSaved);
                         this.sampleAnalysisUseCase.save(sampleAnalysisModel);
                     }

                     // save the entity to register result


                                Integer quantity = analysis.getQuantity();
                                double priceUniteAnalysis = product.getPrice();

                                finalPrice.updateAndGet(v -> v + priceUniteAnalysis * quantity);


                 });

        });



        // add the final price to the testRequest
        testRequestSaved.setPrice(finalPrice.get());
        testRequestSaved.setState(TestRequestConstants.PENDING);


        // create the notification.
         sendNotification(customer, testRequestSaved.getRequestCode());

        return this.testRequestPersistencePort.save(testRequestSaved);
    }

    public void sendNotification(CustomerModel customer, String testRequestCode) {

        this.userUseCase.findAllModels()
                .stream()
                .filter(UserModel::isNotifyQuotes).toList()
                .forEach(userModel -> this.testRequestEmailService.sendEmailNewQuotation(userModel.getEmail(), testRequestCode , userModel.getName(), "http://localhost:5173/system/quotes" ));

        Notifications notification = new Notifications();
        notification.setMessage("Llego una nueva cotizacion de ensayo, codigo " + testRequestCode);
        notification.setActorUser("Cliente - " + customer.getCustomerName());
        notification.setType(TypeNotifications.NEW_QUOTATION);
        notification.setTags(List.of(RoleConstantsNotification.ROLE_ADMIN, RoleConstantsNotification.ROLE_SUPERADMIN, RoleConstantsNotification.ROLE_ANALYST));
        this.notificationsService.saveNotification(notification);

    }

    @Override
    public TestRequestModel update(TestRequestModel testRequestModel) {
        return this.testRequestPersistencePort.save(testRequestModel);
    }

    @Override
    public TestRequestModel getTestRequestById(@Valid Long id) {
        return this.testRequestPersistencePort.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No se encontro el ensayo con ese id"));
    }

    @Override
    public TestRequestModel getByRequestCode(@Valid  String requestCode) {
        return this.testRequestPersistencePort.findByRequestCode(requestCode)
                .orElseThrow(() -> new EntityNotFoundException("No se encontro el ensayo cob codigo: " + requestCode));
    }

    @Override
    public List<TestRequestModel> getAllTestRequestNoCheck() {
        return List.of();
    }

    @Override
    public List<TestRequestModel> getTestRequestsDueToday() {
        LocalDate today = LocalDate.now();
        return this.testRequestPersistencePort.findAllByDueDate(today);
    }

    @Override
    public List<TestRequestModel> getTestRequestDueDateExpired() {
        LocalDate today = LocalDate.now();

        return this.testRequestPersistencePort.findAllByDueDateExpired(today);
    }

    @Override
    public List<TestRequestModel> getAllByState(@Valid String state) {
        if (!state.equalsIgnoreCase(TestRequestConstants.ACCEPTED) &&
                !state.equalsIgnoreCase(TestRequestConstants.PENDING) &&
                !state.equalsIgnoreCase(TestRequestConstants.DELIVERED_AND_FINISHED) &&
                !state.equalsIgnoreCase(TestRequestConstants.REJECTED)) {
            return this.getAllTestRequest();
        }

        return this.testRequestPersistencePort.findAllByState(state);
    }

    @Override
    public List<TestRequestModel> getTestRequestByCode() {
        return List.of();
    }

    @Override
    public Map<String, List<SamplesByTestRequestDto>> getSamplesByTestRequest(Long testRequestId) {

        Boolean existTest = this.testRequestPersistencePort.existsById(testRequestId);

        if(!existTest){
            throw new IllegalArgumentException("No se encontro el ensayao con id: ");
        }

        System.out.println(this.testRequestPersistencePort.findSamplesInfoByTestRequest(testRequestId));

        List<SamplesByTestRequestDto> sampleInfo = this.testRequestPersistencePort.findSamplesInfoByTestRequest(testRequestId).stream()
                .map(proj -> new SamplesByTestRequestDto(
                        proj.getSample(),
                        proj.getAnalysis(),
                        proj.getPriceByAnalysis(),
                        proj.getQuantityAnalysisBySample(),
                        proj.getTotal()
                ))
                .toList();

        Map<String, List<SamplesByTestRequestDto>> samplesAndAnalysis =  new HashMap<>();

        for (SamplesByTestRequestDto sample : sampleInfo){
             samplesAndAnalysis.computeIfAbsent(sample.getSample(), k -> new ArrayList<>())
                     .add(sample);
        }
        return samplesAndAnalysis;
    }

    @Override
    @Transactional
    public void deleteById(@Valid Long testRequestId) {

        if(!this.testRequestPersistencePort.existsById(testRequestId)){
            throw new IllegalArgumentException("No se puede eliminar este ensayo ya que no existe en la base de datos.");
        }

        // delete also the samples with that testRequestId and table of result also
        List<SampleModel> samples = this.sampleUseCase.getAllByTestRequest(testRequestId);
        samples.forEach(sample -> {
           List<SampleAnalysisModel> sampleAnalysis =  this.sampleAnalysisUseCase.getSamplesAnalysisBySample(sample.getSampleId());
           sampleAnalysis.forEach(s -> {
               this.sampleAnalysisUseCase.deleteById(s.getSampleProductAnalysisId());
           });


           this.sampleUseCase.deleteById(sample.getSampleId());
        } );

        // TO DO : add and delete file in cloudinary bro.

        this.testRequestPersistencePort.deleteById(testRequestId);
    }

    @Override
    @Transactional
    public TestRequestModel acceptOrRejectTestRequest(Long testRequestId, Boolean isApproved, String message, String emailCustomer) {
         TestRequestModel testRequest = this.testRequestPersistencePort.findById(testRequestId)
                 .orElseThrow(() -> new EntityNotFoundException("no se encontro el ensayo en la base de datos."));

         if (testRequest.getIsApproved()) throw  new IllegalArgumentException("La solucitud ya fue aceptada");

         testRequest.setIsApproved(isApproved);
         testRequest.setApprovalDate(isApproved ? LocalDate.now() : null);
         testRequest.setDiscardDate(!isApproved ? LocalDate.now() : null);
         testRequest.setState(isApproved ? TestRequestConstants.ACCEPTED : TestRequestConstants.REJECTED);
         testRequest.setIsFinished( isApproved ?  false : null);
         testRequest.setDeliveryStatus( isApproved ? TestRequestConstants.WAITING_FOR_SAMPLE : null);
         
         // TO DO : if accepted send email to the customer
        return this.testRequestPersistencePort.save(testRequest);
    }

    @Override
    public List<TestRequestModel> getAllByOptionAndParam(String option, String param) {

        List<TestRequestModel> list = new ArrayList<>();

        if (option.equalsIgnoreCase("code")) {
            list = this.testRequestPersistencePort.findAllByRequestCode(param);
        } else if (option.equalsIgnoreCase("customerName")) {
            list = this.testRequestPersistencePort.findAllByCustomerName(param);
        } else {
            list = this.testRequestPersistencePort.getAll();
        }


        return list;

    }

    @Override
    public void assignResponsible(List<Long> usersId, Long testRequestId) {

        // find the testRequest
        if(!this.testRequestPersistencePort.existsById(testRequestId)){
            throw  new EntityNotFoundException("La entidad no existe");
        }

          // find all users available and assign to the test request
          List<UserModel> listOfUsers =  this.userUseCase.findAllById(usersId);


       this.testRequestPersistencePort.assignResponsible(testRequestId, listOfUsers);

    }

    @Override
    public Page<TestRequestSummaryInfoResponse> getAllTestRequestSummaryInfo(Pageable pageable) {
        Page<TestRequestModel> testRequests = this.testRequestPersistencePort.findAllTestRequestAccepted(pageable);
        List<TestRequestSummaryInfoResponse> content =
                testRequests.getContent().stream()
                        .map(this::mapSingleTestRequest)
                        .toList();

        return new PageImpl<>(
                content,
                pageable,
                testRequests.getTotalElements()
        );
    }

    private TestRequestSummaryInfoResponse mapSingleTestRequest(TestRequestModel test) {

        int totalSamples = 0;
        int totalAnalysis = 0;
        int totalAnalysisMade = 0;

        for (SampleModel sample : test.getSamples()) {
            totalSamples++;
            for (SampleAnalysisModel analysis : sample.getAnalysisEntities()) {
                totalAnalysis++;
                if (Boolean.TRUE.equals(analysis.getStateResult())) {
                    totalAnalysisMade++;
                }
            }
        }

        double percent = totalAnalysis == 0
                ? 0
                : ((double) totalAnalysisMade / totalAnalysis) * 100;

        List<UserResponseMembersAssigned> team =
                userUseCase.getAllByTestRequest(test.getTestRequestId());

        return new TestRequestSummaryInfoResponse(
                test.getTestRequestId(),
                test.getRequestCode(),
                test.getDeliveryStatus(),
                test.getIsFinished(),
                test.getDueDate(),
                test.getSubmissionDate(),
                test.getApprovalDate(),
                totalAnalysis,
                totalSamples,
                test.getPrice(),
                percent,
                team
        );
    }





    @Override
    public List<TestRequestSummaryInfoResponse> getAllTestRequestSummaryInfoByCode(String code) {
        List<TestRequestModel> testRequestList = this.testRequestPersistencePort.findAllTestRequestByRequestCode(code);
        return this.mapToSummaryResponses(testRequestList);
    }

    @Override
    public List<TestRequestSummaryInfoResponse> getAllTestRequestSummaryInfoByDeliveryState(String state) {
        List<TestRequestModel> testRequests = this.testRequestPersistencePort.findAllByDeliveryState(state);
        if(testRequests.size() < 1){
            return List.of();
        }

        return this.mapToSummaryResponses(testRequests);
    }

    @Override
    public List<SampleModel> getSamples() {
        Long number =  6L;
        return this.sampleUseCase.getAllByTestRequest(number);
    }

    @Override
    public List<UserResponseMembersAssigned> usersAssignedTestRequest(Long testRequestId) {
      if (!this.testRequestPersistencePort.existsById(testRequestId)){
           throw new EntityNotFoundException("No se encontro el ensayo con id : " + testRequestId);
      }
        return  this.userUseCase.usersAssignedTestRequest(testRequestId).stream().map(u -> {
            return new UserResponseMembersAssigned(
                 u.userId(),
                 u.name(),
                 u.available(),
                 u.imageProfile(),
                 u.email()
            );

        }).toList();

    }

    @Override
    @Transactional
    public List<UserResponseMembersAssigned> removeMember(Long userId, Long testRequestId) {
        // validate if the user exist
         if(!this.userUseCase.existById(userId)) throw new EntityNotFoundException("No se pudo procesar este usuario.");


         // validate if the test request exist.
        if(!this.testRequestPersistencePort.existsById(testRequestId)) throw new EntityNotFoundException("No se pudo encontrar este ensayo.");
         // remove the user

        this.testRequestPersistencePort.removeMember(userId, testRequestId);



        return this.userUseCase.getAllByTestRequest(testRequestId);
    }

    @Override
    public void changeStatusDelivery( Long testRequestId) {

        TestRequestModel testRequest = this.testRequestPersistencePort.findById(testRequestId)
                .orElseThrow(() -> new EntityNotFoundException("No se encontro el ensayo con id: "+ testRequestId));

        if(!testRequest.getSamples().isEmpty()){
            Boolean allReadyReception = testRequest.getSamples().stream().allMatch(SampleModel::getStatusReception);

            if(allReadyReception){
                // if all samples are ready in reception, generate the due date.
                testRequest.setDueDate(LocalDate.now().plusDays(15));
                // change the status
                testRequest.setDeliveryStatus(TestRequestConstants.IN_PROGRESS);

                this.testRequestPersistencePort.save(testRequest);
            }
        }

    }


    public List<TestRequestSummaryInfoResponse> mapToSummaryResponses(List<TestRequestModel> testRequest){
        List<TestRequestSummaryInfoResponse> listToReturn  = new ArrayList<>();
        
        for (TestRequestModel test : testRequest){

            int totalSamples = 0;
            int totalAnalysis = 0;
            int totalAnalysisMade = 0;

            for (SampleModel sample : test.getSamples()) {
                totalSamples++;
                for (SampleAnalysisModel analysis : sample.getAnalysisEntities()){
                    totalAnalysis++;
                    if(analysis.getStateResult()){
                        totalAnalysisMade++;
                    }

                }
            }
            double percent = totalAnalysis == 0 ? 0 : ((double) totalAnalysisMade / totalAnalysis) * 100;

            List<UserResponseMembersAssigned> teamForThisTestRequest = this.userUseCase.getAllByTestRequest(test.getTestRequestId());

            TestRequestSummaryInfoResponse testRequestSummaryInfoResponse = new TestRequestSummaryInfoResponse(
                    test.getTestRequestId(),
                    test.getRequestCode(),
                    test.getDeliveryStatus(),
                    test.getIsFinished(),
                    test.getDueDate(),
                    test.getSubmissionDate(),
                    test.getApprovalDate(),
                    totalAnalysis,
                    totalSamples,
                    test.getPrice(),
                    percent,
                    teamForThisTestRequest
            );

            listToReturn.add(testRequestSummaryInfoResponse);

        }

        return listToReturn;
    }


}
