package com.example.sennova.application.event.handler;

import com.example.sennova.application.usecases.TestRequest.TestRequestUseCase;
import com.example.sennova.domain.event.SampleReceptionUpdateEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class SampleReceptionUpdateHandler {
    private final TestRequestUseCase testRequestUseCase;

    public SampleReceptionUpdateHandler(TestRequestUseCase testRequestUseCase) {
        this.testRequestUseCase = testRequestUseCase;
    }

    @EventListener
    public void handle(SampleReceptionUpdateEvent event){
        this.testRequestUseCase.changeStatusDelivery(event.getTestRequestId());
    }
}
