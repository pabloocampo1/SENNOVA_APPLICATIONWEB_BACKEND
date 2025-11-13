package com.example.sennova.application.dto.testeRequest;

import java.util.List;

public record SampleRequestRecord(
    String matrix,
    String description,
    List<ProductQuantityQuote> analysis
) {
}
