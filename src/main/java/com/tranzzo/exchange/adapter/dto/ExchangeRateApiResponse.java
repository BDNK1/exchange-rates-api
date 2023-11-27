package com.tranzzo.exchange.adapter.dto;

import java.math.BigDecimal;
import java.util.Map;

public record ExchangeRateApiResponse(
        boolean success,
        long timestamp,
        String base,
        Map<String, BigDecimal> rates
) {
}
