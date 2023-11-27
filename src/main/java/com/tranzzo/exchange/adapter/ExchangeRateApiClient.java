package com.tranzzo.exchange.adapter;

import com.tranzzo.exchange.adapter.dto.ExchangeRateApiResponse;
import com.tranzzo.exchange.domain.CurrencyCode;
import com.tranzzo.exchange.properties.ExchangeRateApiProperties;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Arrays;

import static java.util.stream.Collectors.joining;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ExchangeRateApiClient {

    ExchangeRateApiProperties properties;

    public Mono<ExchangeRateApiResponse> getLatest() {
        String currencies = Arrays.stream(CurrencyCode.values()).map(CurrencyCode::name).collect(joining(","));
        return WebClient.create(properties.url())
                .get()
                .uri(uriBuilder -> uriBuilder.path("/v1/latest")
                        .queryParam("access_key", properties.apiKey())
                        .queryParam("symbols", currencies)
                        .build())
                .retrieve()
                .bodyToMono(ExchangeRateApiResponse.class)
                .retry(2)
                .onErrorMap(err -> new RuntimeException("Failed to get exchange rates", err));

    }
}
