package com.example.sennova.infrastructure.projection;

public interface SamplesDeliveredLast12MonthsProjection {
    String getDeliveryDate();
    Long getTotal();
}
