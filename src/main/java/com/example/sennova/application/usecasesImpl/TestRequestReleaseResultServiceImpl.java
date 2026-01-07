package com.example.sennova.application.usecasesImpl;

import com.example.sennova.application.dto.testeRequest.ReleaaseResult.InfoResponsiblePersonReleaseResult;
import com.example.sennova.application.usecases.SampleUseCase;
import com.example.sennova.application.usecases.TestRequest.TestRequestReleaseResultUseCase;
import com.example.sennova.application.usecases.TestRequest.TestRequestUseCase;
import com.example.sennova.domain.constants.TestRequestConstants;
import com.example.sennova.domain.event.DomainEventPublisher;
import com.example.sennova.domain.event.SampleSendReportEvent;
import com.example.sennova.domain.model.testRequest.CustomerModel;
import com.example.sennova.domain.model.testRequest.SampleModel;
import com.example.sennova.domain.model.testRequest.TestRequestModel;
import com.example.sennova.infrastructure.persistence.entities.analysisRequestsEntities.ReportDeliverySample;
import com.example.sennova.infrastructure.persistence.repositoryJpa.ReportDeliveryStatusRepositoryJpa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TestRequestReleaseResultServiceImpl implements TestRequestReleaseResultUseCase {

    private final TestRequestUseCase testRequestUseCase;
    private final SampleUseCase sampleUseCase;
    private final ReleaseResultGeneratePdfService releaseResultGeneratePdfService;
    private final DomainEventPublisher domainEventPublisher;
    private final ReportDeliveryStatusRepositoryJpa reportDeliveryStatusRepositoryJpa;

    @Autowired
    public TestRequestReleaseResultServiceImpl(TestRequestUseCase testRequestUseCase, SampleUseCase sampleUseCase, ReleaseResultGeneratePdfService releaseResultGeneratePdfService, DomainEventPublisher domainEventPublisher, ReportDeliveryStatusRepositoryJpa reportDeliveryStatusRepositoryJpa) {
        this.testRequestUseCase = testRequestUseCase;
        this.sampleUseCase = sampleUseCase;

        this.releaseResultGeneratePdfService = releaseResultGeneratePdfService;
        this.domainEventPublisher = domainEventPublisher;
        this.reportDeliveryStatusRepositoryJpa = reportDeliveryStatusRepositoryJpa;
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
    public List<byte[]> generateReleaseResultByTestRequest(Long testRequestId) {
        return List.of();
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
                   .requestCode(s.getTestRequest().getRequestCode())
                   .responsibleName(infoResponsiblePersonReleaseResult.getName())
                   .status(TestRequestConstants.PENDING_DELIVERY)
                   .build();

           ReportDeliverySample reportSaved = this.reportDeliveryStatusRepositoryJpa.save(reportDeliverySample);

           this.domainEventPublisher.publish(new SampleSendReportEvent(s.getSampleId(), infoResponsiblePersonReleaseResult , reportPdf, reportSaved.getId() ));

               }

        );


    }

    @Override
    public void generateAndSendTestRequestReport(Long testRequestId, InfoResponsiblePersonReleaseResult infoResponsiblePersonReleaseResult) {
//        if(resultExecutionFinalTestRequestDto.getDocuments().size() >= 3) throw  new IllegalArgumentException(("No puedes enviar mas de 2 documentos para el informe final del cliente"));
//
//        TestRequestModel testRequest = this.getTestRequestById(resultExecutionFinalTestRequestDto.getTestRequestId());
//
//        List<SampleModel> samples = this.getSamples();
//        CustomerModel customer = testRequest.getCustomer();
//
//
//        // validate if all result are complete
//        Boolean isCompleted = samples.stream().allMatch(
//                s -> s.getAnalysisEntities().stream()
//                        .allMatch(SampleAnalysisModel::getStateResult)
//        );
//
//        if(!isCompleted) throw new IllegalArgumentException(("No puedes emitir el reporte final sin todos los analisis del ensayo completados."));
//
//
//
////        byte[] finalReport =  this.pdfService.generarInformePdf();
//
//        testRequest.setIsFinished(true);
//
//        this.testRequestPersistencePort.save(testRequest);
//
////        this.testRequestEmailService.sendFinalReport()



    }
}
