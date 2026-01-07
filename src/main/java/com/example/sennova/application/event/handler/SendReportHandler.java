package com.example.sennova.application.event.handler;

import com.example.sennova.application.usecases.SampleUseCase;
import com.example.sennova.domain.constants.TestRequestConstants;
import com.example.sennova.domain.event.SampleSendReportEvent;
import com.example.sennova.domain.model.testRequest.SampleModel;
import com.example.sennova.infrastructure.persistence.entities.analysisRequestsEntities.ReportDeliverySample;
import com.example.sennova.infrastructure.persistence.repositoryJpa.ReportDeliveryStatusRepositoryJpa;
import com.example.sennova.infrastructure.restTemplate.TestRequestEmailService;
import com.example.sennova.web.exception.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;


import java.time.LocalDateTime;

@Component
public class SendReportHandler {

    private final SampleUseCase sampleUseCase;
    private final TestRequestEmailService emailService;
    private final ReportDeliveryStatusRepositoryJpa reportDeliveryStatusRepositoryJpa;


    @Autowired
    public SendReportHandler(SampleUseCase sampleUseCase, TestRequestEmailService testRequestEmailService, ReportDeliveryStatusRepositoryJpa reportDeliveryStatusRepositoryJpa) {
        this.sampleUseCase = sampleUseCase;
        this.emailService = testRequestEmailService;
        this.reportDeliveryStatusRepositoryJpa = reportDeliveryStatusRepositoryJpa;
    }


    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handle(SampleSendReportEvent event) {
        SampleModel sample = this.sampleUseCase.getById(event.getSampleId());
        ReportDeliverySample reportDeliverySample = this.reportDeliveryStatusRepositoryJpa.findById(event.getReportDeliverySampleId())
                .orElseThrow(() -> new EntityNotFoundException("No se encontro el registro"));

        try{

               this.emailService.sendSampleReport(
                       sample.getTestRequest().getCustomer(),
                       event.getPdfDocument(),
                       event.getInfoResponsiblePersonReleaseResult().getName(),
                       sample.getSampleCode()
               );


               reportDeliverySample.setStatus(TestRequestConstants.SENT);
               reportDeliverySample.setSentAt(LocalDateTime.now());
               reportDeliveryStatusRepositoryJpa.save(reportDeliverySample);

               // update the sample

               sample.setIsDelivered(true);
               this.sampleUseCase.save(sample);

               // create event to verify if all samples in one test request are send, if is right, change the status
               
           } catch (Exception e) {

               reportDeliverySample.setStatus(TestRequestConstants.FAILED);
               reportDeliveryStatusRepositoryJpa.save(reportDeliverySample);

               sample.setIsDelivered(false);
               this.sampleUseCase.save(sample);

               e.printStackTrace();
           }
    }
}
