package com.example.sennova.application.event.handler;

import com.example.sennova.application.usecases.TestRequest.TestRequestUseCase;
import com.example.sennova.domain.constants.TestRequestConstants;
import com.example.sennova.domain.event.TestRequestDeliveredEvent;
import com.example.sennova.domain.model.testRequest.SampleModel;
import com.example.sennova.domain.model.testRequest.TestRequestModel;
import com.example.sennova.domain.port.TestRequestPersistencePort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
public class TestRequestDeliveredHandler {

   private final TestRequestPersistencePort testRequestPersistencePort;


   @Autowired
    public TestRequestDeliveredHandler(TestRequestPersistencePort testRequestPersistencePort) {
        this.testRequestPersistencePort = testRequestPersistencePort;
    }

    @Async
    @EventListener
    @Transactional
    public void handle(TestRequestDeliveredEvent event) {

        long pending = testRequestPersistencePort.countNotDeliveredSamples(event.getTestRequestId());

        if (pending > 0) {
            return;
        }

        testRequestPersistencePort.findById(event.getTestRequestId())
                .ifPresent(tr -> {
                    tr.setDeliveryStatus(TestRequestConstants.DELIVERED_AND_FINISHED);
                    testRequestPersistencePort.save(tr);
                });
    }

}
