package com.example.sennova.application.dto.testeRequest;

import java.util.List;

public record TestRequestRecord(
    List<SampleRequestRecord> samples,
    CustomerRequestRecord customer
) {

}