package com.example.sennova.application.usecasesImpl;

import com.example.sennova.application.dto.testeRequest.ReleaaseResult.InfoResponsiblePersonReleaseResult;
import com.example.sennova.application.dto.testeRequest.ResultExecutionFinalTestRequestDto;
import com.example.sennova.application.usecases.SampleUseCase;
import com.example.sennova.application.usecases.TestRequest.TestRequestReleaseResultUseCase;
import com.example.sennova.application.usecases.TestRequest.TestRequestUseCase;
import com.example.sennova.domain.constants.TestRequestConstants;
import com.example.sennova.domain.event.DomainEventPublisher;
import com.example.sennova.domain.event.SampleSendReportEvent;
import com.example.sennova.domain.model.testRequest.CustomerModel;
import com.example.sennova.domain.model.testRequest.SampleAnalysisModel;
import com.example.sennova.domain.model.testRequest.SampleModel;
import com.example.sennova.domain.model.testRequest.TestRequestModel;
import com.example.sennova.infrastructure.persistence.entities.analysisRequestsEntities.ReportDeliverySample;
import com.example.sennova.infrastructure.persistence.repositoryJpa.ReportDeliveryStatusRepositoryJpa;
import com.example.sennova.infrastructure.restTemplate.TestRequestEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    public TestRequestReleaseResultServiceImpl(TestRequestUseCase testRequestUseCase, TestRequestEmailService testRequestEmailService, SampleUseCase sampleUseCase, ReleaseResultGeneratePdfService releaseResultGeneratePdfService, DomainEventPublisher domainEventPublisher, ReportDeliveryStatusRepositoryJpa reportDeliveryStatusRepositoryJpa) {
        this.testRequestUseCase = testRequestUseCase;
        this.testRequestEmailService = testRequestEmailService;
        this.sampleUseCase = sampleUseCase;

        this.releaseResultGeneratePdfService = releaseResultGeneratePdfService;
        this.domainEventPublisher = domainEventPublisher;
        this.reportDeliveryStatusRepositoryJpa = reportDeliveryStatusRepositoryJpa;
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
                   .requestCode(s.getTestRequest().getRequestCode())
                   .responsibleName(infoResponsiblePersonReleaseResult.getName())
                   .status(TestRequestConstants.PENDING_DELIVERY)
                   .build();

           ReportDeliverySample reportSaved = this.reportDeliveryStatusRepositoryJpa.save(reportDeliverySample);

           this.domainEventPublisher.publish(new SampleSendReportEvent(s.getSampleId(), infoResponsiblePersonReleaseResult , reportPdf, reportSaved.getId() ));

               }

        );


    }

//    esto metodo permite finalizar y enviar los resultados finales de un ensayo al cliente

    @Override
    @Transactional
    public List<byte[]> generateAndSendTestRequestReport(ResultExecutionFinalTestRequestDto resultExecutionFinalTestRequestDto) {


        if (resultExecutionFinalTestRequestDto.getDocuments() != null
                && resultExecutionFinalTestRequestDto.getDocuments().size() >= 3) {
            throw new IllegalArgumentException(
                    "No puedes enviar más de 2 documentos para el informe final del cliente"
            );
        }


        try{
            TestRequestModel testRequest = this.testRequestUseCase.getByRequestCode(resultExecutionFinalTestRequestDto.getRequestCode());

            List<SampleModel> samples = testRequest.getSamples();

            // validate if all result are complete
            Boolean isCompleted = samples.stream().allMatch(
                    s -> s.getAnalysisEntities().stream()
                            .allMatch(SampleAnalysisModel::getStateResult)
            );

            if(!isCompleted) throw new IllegalArgumentException(("No puedes emitir el reporte final sin todos los analisis del ensayo completados."));

            InfoResponsiblePersonReleaseResult responsible = new InfoResponsiblePersonReleaseResult(
                    resultExecutionFinalTestRequestDto.getResponsibleName(),
                    resultExecutionFinalTestRequestDto.getRole(),
                    resultExecutionFinalTestRequestDto.getSignatureImage()
            );


            // generate all de documents
            List<byte[]> listGeneratedReports = samples.stream().map(s -> this.generateReleaseResultBySampleId(
                    s,
                    responsible)).toList();

            // change the status of each sample to delivered = true
            testRequest.getSamples().forEach(s -> {
                s.setDeliveryDate(LocalDateTime.now());
                s.setIsDelivered(true);

                // create also the register of history send
            });


            // change the status delivered of the request to completed and delivereed

            testRequest.setDeliveryStatus(TestRequestConstants.DELIVERED_AND_FINISHED);



            // sent the email
            // crear el event para poder guardar
            this.testRequestEmailService.sendFinalReport(
                    testRequest.getCustomer(),
                    testRequest.getRequestCode(),
                    listGeneratedReports,
                    resultExecutionFinalTestRequestDto.getDocuments(),
                    resultExecutionFinalTestRequestDto.getNotes(),
                    resultExecutionFinalTestRequestDto.getResponsibleName(),
                    resultExecutionFinalTestRequestDto.getRole()
            );
            this.testRequestUseCase.update(testRequest);
            return listGeneratedReports;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }



    }

    @Override
    public List<ReportDeliverySample> getHistoryDeliveryByRequestCode(String requestCode) {
        return this.reportDeliveryStatusRepositoryJpa.findAllByRequestCode(requestCode);
    }
}
