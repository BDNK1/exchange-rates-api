package com.tranzzo.exchange.properties;

import com.tranzzo.exchange.domain.CurrencyCode;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "exchange-rate-api")
public record ExchangeRateApiProperties(
        String url,
        String key,
        CurrencyCode baseCurrency,
        String apiKey) {
}
