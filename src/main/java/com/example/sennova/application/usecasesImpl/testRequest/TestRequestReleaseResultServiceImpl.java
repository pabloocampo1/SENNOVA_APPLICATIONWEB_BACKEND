package com.example.sennova.application.usecasesImpl.testRequest;

import com.example.sennova.application.dto.testeRequest.ReleaaseResult.InfoResponsiblePersonReleaseResult;
import com.example.sennova.application.dto.testeRequest.ResultExecutionFinalTestRequestDto;
import com.example.sennova.application.usecases.SampleUseCase;
import com.example.sennova.application.usecases.TestRequest.TestRequestReleaseResultUseCase;
import com.example.sennova.application.usecases.TestRequest.TestRequestUseCase;
import com.example.sennova.application.usecases.TestRequest.TestRequestValidatorUseCase;
import com.example.sennova.domain.constants.TestRequestConstants;
import com.example.sennova.domain.event.DomainEventPublisher;
import com.example.sennova.domain.event.SampleSendReportEvent;
import com.example.sennova.domain.model.testRequest.CustomerModel;
import com.example.sennova.domain.model.testRequest.SampleModel;
import com.example.sennova.domain.model.testRequest.TestRequestModel;
import com.example.sennova.infrastructure.persistence.entities.analysisRequestsEntities.ReportDeliverySample;
import com.example.sennova.infrastructure.persistence.repositoryJpa.ReportDeliveryStatusRepositoryJpa;
import com.example.sennova.infrastructure.restTemplate.TestRequestEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TestRequestReleaseResultServiceImpl implements TestRequestReleaseResultUseCase {

    private final TestRequestUseCase testRequestUseCase;
    private final TestRequestEmailService testRequestEmailService;
    private final SampleUseCase sampleUseCase;
    private final ReleaseResultGeneratePdfService releaseResultGeneratePdfService;
    private final DomainEventPublisher domainEventPublisher;
    private final ReportDeliveryStatusRepositoryJpa reportDeliveryStatusRepositoryJpa;
    private final TestRequestValidatorUseCase testRequestValidatorUseCase;


    @Autowired
    public TestRequestReleaseResultServiceImpl(TestRequestUseCase testRequestUseCase, TestRequestEmailService testRequestEmailService, SampleUseCase sampleUseCase, ReleaseResultGeneratePdfService releaseResultGeneratePdfService, DomainEventPublisher domainEventPublisher, ReportDeliveryStatusRepositoryJpa reportDeliveryStatusRepositoryJpa, TestRequestValidatorUseCase testRequestValidatorUseCase) {
        this.testRequestUseCase = testRequestUseCase;
        this.testRequestEmailService = testRequestEmailService;
        this.sampleUseCase = sampleUseCase;

        this.releaseResultGeneratePdfService = releaseResultGeneratePdfService;
        this.domainEventPublisher = domainEventPublisher;
        this.reportDeliveryStatusRepositoryJpa = reportDeliveryStatusRepositoryJpa;
        this.testRequestValidatorUseCase = testRequestValidatorUseCase;
    }

    @Override
    public byte[] generateReleaseResultBySampleIdPreview(Long sampleId) {
        try {

            InfoResponsiblePersonReleaseResult infoResponsiblePersonReleaseResult = new InfoResponsiblePersonReleaseResult(
                    "Vista previa",
                    "Vista previa",
                    null
            );

            SampleModel sample = this.sampleUseCase.getById(sampleId);

            return this.generateReleaseResultBySampleId(sample, infoResponsiblePersonReleaseResult);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public byte[] generateReleaseResultBySampleId(SampleModel sample, InfoResponsiblePersonReleaseResult infoResponsiblePersonReleaseResult) {
        try {
            CustomerModel customerModel = sample.getTestRequest().getCustomer();
            TestRequestModel testRequestModel = sample.getTestRequest();
            return this.releaseResultGeneratePdfService.generateReportBySample(sample, customerModel, testRequestModel, infoResponsiblePersonReleaseResult);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
       }


       

    @Override
    @Transactional
    public void generateAndSendSampleReport(List<Long> samples, InfoResponsiblePersonReleaseResult infoResponsiblePersonReleaseResult) {
        List<SampleModel> samplesListModels =  this.sampleUseCase.getAllSamplesById(samples);

       samplesListModels.stream().forEach( s -> {

           // check if the sample have all analysis finished

            if(!this.sampleUseCase.checkIfAllAnalyisisAreFinished(s)) throw  new IllegalArgumentException("Unas de las muestras aun no ah emitido resultado de analysis");

            byte[] reportPdf = this.generateReleaseResultBySampleId(s, infoResponsiblePersonReleaseResult);
           ReportDeliverySample reportDeliverySample = ReportDeliverySample.builder()
                   .sampleId(s.getSampleId())
                   .sampleCode(s.getSampleCode())
                   .matrixSample(s.getMatrix())
                   .totalAnalysis(s.getAnalysisEntities().size())
                   .customerName(s.getTestRequest().getCustomer().getCustomerName())
                   .customerEmail(s.getTestRequest().getCustomer().getEmail())
                   .testRequestId(s.getTestRequest().getTestRequestId())
                   .requestCode(s.getTestRequest().getRequestCode())
                   .responsibleName(infoResponsiblePersonReleaseResult.getName())
                   .status(TestRequestConstants.PENDING_DELIVERY)
                   .build();

           ReportDeliverySample reportSaved = this.reportDeliveryStatusRepositoryJpa.save(reportDeliverySample);

           this.domainEventPublisher.publish(new SampleSendReportEvent(s.getSampleId(), infoResponsiblePersonReleaseResult , reportPdf, reportSaved.getId() ));

               }

        );


    }


    // Finalizes the test request flow: Validates business rules, generates PDF reports,
    // logs delivery history, and triggers the email dispatch synchronously.
    // NOTE: Updates delivery status to FAILED within the catch block to ensure traceability
    // in case of SMTP or network errors.
    @Override
    @Transactional
    public void generateAndSendTestRequestReport(ResultExecutionFinalTestRequestDto dto) {

        // 1.  init validations
        this.testRequestValidatorUseCase.validateDocumentsCount(dto.getDocuments());
        TestRequestModel testRequest = testRequestUseCase.getByRequestCode(dto.getRequestCode());
        this.testRequestValidatorUseCase.validateAllAnalysisComplete(testRequest.getSamples());

        // 2. build the responsible info
        InfoResponsiblePersonReleaseResult responsible = new InfoResponsiblePersonReleaseResult(
                dto.getResponsibleName(), dto.getRole(), dto.getSignatureImage());

        // 3. generate reports
        List<byte[]> listGeneratedReports = testRequest.getSamples().stream()
                .map(s -> generateReleaseResultBySampleId(s, responsible))
                .toList();

        // 4. update and create the history of delivery (database)
        List<ReportDeliverySample> history =
                testRequest.getSamples()
                        .stream()
                        .map(s -> ReportDeliverySample.builder()
                                .sampleId(s.getSampleId())
                                .sampleCode(s.getSampleCode())
                                .matrixSample(s.getMatrix())
                                .totalAnalysis(s.getAnalysisEntities().size())
                                .customerName(s.getTestRequest().getCustomer().getCustomerName())
                                .customerEmail(s.getTestRequest().getCustomer().getEmail())
                                .testRequestId(s.getTestRequest().getTestRequestId())
                                .requestCode(s.getTestRequest().getRequestCode())
                                .responsibleName(responsible.getName())
                                .status(TestRequestConstants.PENDING_DELIVERY)
                                .build()  )
                        .toList();



        this.reportDeliveryStatusRepositoryJpa.saveAll(history);


        // 5. Send email
        try {
            this.testRequestEmailService.sendFinalReport(
                    testRequest.getCustomer(),
                    testRequest.getRequestCode(),
                    listGeneratedReports,
                    dto.getDocuments(),
                    dto.getNotes(),
                    dto.getResponsibleName(),
                    dto.getRole()
            );

            // update history report to SENT
            history.forEach(h -> {
                h.setSentAt(LocalDateTime.now());
                h.setStatus(TestRequestConstants.SENT);
            });
            this.reportDeliveryStatusRepositoryJpa.saveAll(history);

            testRequest.setDeliveryStatus(TestRequestConstants.DELIVERED_AND_FINISHED);
            testRequest.setSubmissionDate(LocalDate.now());

            // update the samples, mark as a delivered
            LocalDateTime currentTime = LocalDateTime.now();
            testRequest.getSamples().forEach(s -> {
                s.setIsDelivered(true);
                s.setDeliveryDate(currentTime);
            });
            this.testRequestUseCase.update(testRequest);


        } catch (Exception e) {

            history.forEach(h -> h.setStatus(TestRequestConstants.FAILED));
            this.reportDeliveryStatusRepositoryJpa.saveAll(history);

            throw new RuntimeException("Reportes generados pero el envío falló: " + e.getMessage());
        }

       
    }

    @Override
    public List<ReportDeliverySample> getHistoryDeliveryByRequestCode(String requestCode) {
        return this.reportDeliveryStatusRepositoryJpa.findAllByRequestCode(requestCode);
    }
}
