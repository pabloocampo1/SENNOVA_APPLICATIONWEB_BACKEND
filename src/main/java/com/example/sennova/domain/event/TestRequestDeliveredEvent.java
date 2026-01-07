package com.example.sennova.domain.event;

public class TestRequestDeliveredEvent {
    private Long testRequestId;

    public TestRequestDeliveredEvent(Long testRequestId) {
        this.testRequestId = testRequestId;
    }

    public Long getTestRequestId() {
        return testRequestId;
    }
}
