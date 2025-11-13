package com.example.sennova.application.dto.testeRequest;

import com.example.sennova.application.dto.UserDtos.UserResponse;
import com.example.sennova.domain.model.UserModel;

import java.time.LocalDate;
import java.util.List;

public record TestRequestSummaryInfoResponse(
        Long testRequestId,
        String requestCode,
        String deliveryStatus,
        Boolean isFinished,
        LocalDate dueDate,
        LocalDate submissionDate,
        LocalDate approvalDate,
        Integer totalAnalysis,
        Integer totalSample,
        double price  ,
        double progress,
        List<UserResponse> teamAssigned
) {
}
