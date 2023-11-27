package com.tranzzo.exchange.domain;

import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

@Data
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@Table("currency_exchange_rate")
public class ExchangeRate {

    CurrencyCode currencyFrom;
    CurrencyCode currencyTo;
    BigDecimal rate;
    Long timestamp;

    public ExchangeRate(CurrencyCode currencyFrom, CurrencyCode currencyTo, BigDecimal rate, Long timestamp) {
        this.currencyFrom = currencyFrom;
        this.currencyTo = currencyTo;
        this.rate = rate;
        this.timestamp = timestamp;
    }
}
