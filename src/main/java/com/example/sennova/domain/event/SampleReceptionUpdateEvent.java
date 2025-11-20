package com.example.sennova.domain.event;



public class SampleReceptionUpdateEvent {
    private final Long sampleId;
    private final Long testRequestId;

    public SampleReceptionUpdateEvent(Long sampleId, Long testRequestId) {
        this.sampleId = sampleId;
        this.testRequestId = testRequestId;
    }

    public Long getSampleId() { return sampleId; }
    public Long getTestRequestId() { return testRequestId; }
}

