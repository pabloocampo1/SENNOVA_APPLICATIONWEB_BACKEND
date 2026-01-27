package com.example.sennova.application.dto.dashboard;

import lombok.Data;

@Data
public class SamplesDeliveredLast12MonthsDto {
    private String deliveryDate;
    private Long total;
}
