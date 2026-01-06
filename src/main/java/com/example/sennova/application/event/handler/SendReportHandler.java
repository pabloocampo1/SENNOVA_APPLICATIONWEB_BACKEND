package com.example.sennova.application.event.handler;

import com.example.sennova.application.usecases.SampleUseCase;
import com.example.sennova.domain.constants.TestRequestConstants;
import com.example.sennova.domain.event.SampleSendReportEvent;
import com.example.sennova.domain.model.testRequest.SampleModel;
import com.example.sennova.infrastructure.persistence.entities.analysisRequestsEntities.ReportDeliverySample;
import com.example.sennova.infrastructure.persistence.repositoryJpa.ReportDeliveryStatusRepositoryJpa;
import com.example.sennova.infrastructure.restTemplate.TestRequestEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;


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
   @EventListener
    public void handle(SampleSendReportEvent event) {
           try{
               this.emailService.sendSampleReport(
                       event.getSampleModel().getTestRequest().getCustomer(),
                       event.getPdfDocument(),
                       event.getInfoResponsiblePersonReleaseResult().getName(),
                       event.getSampleModel().getSampleCode()
               );

               ReportDeliverySample delivery = event.getReportDeliverySample();
               delivery.setStatus(TestRequestConstants.SENT);
               delivery.setSentAt(LocalDateTime.now());
               reportDeliveryStatusRepositoryJpa.save(delivery);

               // update the sample

               SampleModel sampleModel = event.getSampleModel();
               sampleModel.setIsDelivered(true);
               this.sampleUseCase.save(sampleModel);
               
           } catch (Exception e) {
               ReportDeliverySample delivery = event.getReportDeliverySample();
               delivery.setStatus(TestRequestConstants.FAILED);
               reportDeliveryStatusRepositoryJpa.save(delivery);

               SampleModel sampleModel = event.getSampleModel();
               sampleModel.setIsDelivered(false
               );
               this.sampleUseCase.save(sampleModel);

               e.printStackTrace();
           }
    }
}
