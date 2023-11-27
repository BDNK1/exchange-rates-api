package com.tranzzo.exchange.api.dto;

import com.tranzzo.exchange.domain.CurrencyCode;

import java.math.BigDecimal;

public record ExchangeRateDto(
        CurrencyCode currencyFrom,
        CurrencyCode currencyTo,
        BigDecimal rate
){}
