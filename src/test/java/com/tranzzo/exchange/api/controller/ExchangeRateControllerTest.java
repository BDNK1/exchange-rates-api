package com.tranzzo.exchange.api.controller;

import com.tranzzo.exchange.api.controller.mapper.ExchangeRateMapper;
import com.tranzzo.exchange.api.dto.ExchangeRateDto;
import com.tranzzo.exchange.domain.CurrencyCode;
import com.tranzzo.exchange.domain.ExchangeRate;
import com.tranzzo.exchange.service.ExchangeRateService;
import com.tranzzo.exchange.util.ModelUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@Import(MapperConfig.class)
@WebFluxTest(ExchangeRateController.class)
class ExchangeRateControllerTest {

    @Autowired
    WebTestClient webClient;

    @MockBean
    ExchangeRateService exchangeRateService;

    @Test
    void getExchangeRates() {
        var randomExchangeRates = ModelUtil.generateList(3, () -> ModelUtil.random(ExchangeRate.class));
        when(exchangeRateService.findAll()).thenReturn(Flux.fromIterable(randomExchangeRates));

        ExchangeRateMapper mapper = Mappers.getMapper(ExchangeRateMapper.class);
        List<ExchangeRateDto> dtos = randomExchangeRates.stream().map(mapper::toDto).toList();
        webClient.get()
                .uri("/api/exchange-rates")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .returnResult(ExchangeRateDto.class)
                .consumeWith(res -> res.getResponseBody().as(StepVerifier::create)
                        .expectNextSequence(dtos));
    }

    @Test
    void getExchangeRatesFromCurrency() {
        var randomExchangeRates = ModelUtil.generateList(3, () -> ModelUtil.random(ExchangeRate.class));
        when(exchangeRateService.findAll(CurrencyCode.UAH)).thenReturn(Flux.fromIterable(randomExchangeRates));

        ExchangeRateMapper mapper = Mappers.getMapper(ExchangeRateMapper.class);
        List<ExchangeRateDto> dtos = randomExchangeRates.stream().map(mapper::toDto).toList();

        webClient.get()
                .uri(uri -> uri.path("/api/exchange-rates")
                        .queryParam("currencyFrom", "UAH")
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .returnResult(ExchangeRateDto.class)
                .consumeWith(res -> res.getResponseBody().as(StepVerifier::create)
                        .expectNextSequence(dtos));
    }

    @Test
    void getExchangeRatesByCurrencies() {
        var randomExchangeRate = ModelUtil.random(ExchangeRate.class);
        when(exchangeRateService.findAll(CurrencyCode.UAH, CurrencyCode.USD)).thenReturn(Mono.just(randomExchangeRate));

        ExchangeRateMapper mapper = Mappers.getMapper(ExchangeRateMapper.class);
        ExchangeRateDto dto = mapper.toDto(randomExchangeRate);

        webClient.get()
                .uri(uri -> uri.path("/api/exchange-rates")
                        .queryParam("currencyFrom", "UAH")
                        .queryParam("currencyTo", "USD")
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .returnResult(ExchangeRateDto.class)
                .consumeWith(res -> res.getResponseBody().log().as(StepVerifier::create)
                        .expectNext(dto));
    }

    @Test
    void getExchangeRates_whenCurrencyIsUnsupported() {
        webClient.get()
                .uri(uri -> uri.path("/api/exchange-rates")
                        .queryParam("currencyFrom", "OOO")
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().is4xxClientError()
                .returnResult(String.class)
                .consumeWith(res -> res.getResponseBody().as(StepVerifier::create)
                        .expectNext("Unsupported currency: Currency code OOO is not supported"));
    }


}
