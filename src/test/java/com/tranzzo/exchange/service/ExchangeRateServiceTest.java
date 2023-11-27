package com.tranzzo.exchange.service;

import com.tranzzo.exchange.adapter.ExchangeRateApiClient;
import com.tranzzo.exchange.adapter.dto.ExchangeRateApiResponse;
import com.tranzzo.exchange.domain.CurrencyCode;
import com.tranzzo.exchange.domain.ExchangeRate;
import com.tranzzo.exchange.repository.ExchangeRateRepository;
import com.tranzzo.exchange.util.ModelUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
class ExchangeRateServiceTest {

    @InjectMocks
    ExchangeRateService exchangeRateService;

    @Mock
    ExchangeRateRepository repository;
    @Mock
    ExchangeRateApiClient apiClient;

    @Test
    void findAll() {
        var randomExchangeRate = ModelUtil.random(ExchangeRate.class);
        Mockito.when(repository.findAllLatest()).thenReturn(Flux.just(randomExchangeRate));
        StepVerifier.create(exchangeRateService.findAll())
                .expectNext(randomExchangeRate)
                .verifyComplete();
    }

    @Test
    void findAllByCurrencyFrom() {
        var randomExchangeRate = ModelUtil.random(ExchangeRate.class);
        Mockito.when(repository.findAllLatest(CurrencyCode.USD)).thenReturn(Flux.just(randomExchangeRate));
        StepVerifier.create(exchangeRateService.findAll(CurrencyCode.USD))
                .expectNext(randomExchangeRate)
                .verifyComplete();
    }

    @Test
    void findAllByCurrencies() {
        var randomExchangeRate = ModelUtil.random(ExchangeRate.class);
        Mockito.when(repository.findAllLatest(CurrencyCode.USD, CurrencyCode.EUR)).thenReturn(Mono.just(randomExchangeRate));
        StepVerifier.create(exchangeRateService.findAll(CurrencyCode.USD, CurrencyCode.EUR))
                .expectNext(randomExchangeRate)
                .verifyComplete();
    }

    @Test
    void findAllByCurrencies_whenCurrenciesAreTheSame() {
        StepVerifier.create(exchangeRateService.findAll(CurrencyCode.USD, CurrencyCode.USD))
                .expectNextMatches(exchangeRate -> exchangeRate.getRate().equals(BigDecimal.ONE))
                .verifyComplete();

        Mockito.verifyNoInteractions(repository);
    }

    @Test
    void updateExchangeRates() {
        Map<String, BigDecimal> rates = Map.of("USD", BigDecimal.TWO, "UAH", BigDecimal.TEN);
        long timestamp = 123456789L;
        var randomExchangeRateResponse = new ExchangeRateApiResponse(true, timestamp, "EUR", rates);
        List<ExchangeRate> allCombinations = List.of(
                new ExchangeRate(CurrencyCode.EUR, CurrencyCode.USD, BigDecimal.TWO, timestamp),
                new ExchangeRate(CurrencyCode.EUR, CurrencyCode.UAH, BigDecimal.TEN, timestamp),
                new ExchangeRate(CurrencyCode.USD, CurrencyCode.EUR, BigDecimal.valueOf(0.5).setScale(10), timestamp),
                new ExchangeRate(CurrencyCode.USD, CurrencyCode.UAH, BigDecimal.valueOf(5).setScale(10), timestamp),
                new ExchangeRate(CurrencyCode.UAH, CurrencyCode.EUR, BigDecimal.valueOf(0.1).setScale(10), timestamp),
                new ExchangeRate(CurrencyCode.UAH, CurrencyCode.USD, BigDecimal.valueOf(0.2).setScale(10), timestamp)
        );

        Mockito.when(apiClient.getLatest()).thenReturn(Mono.just(randomExchangeRateResponse));
        Mockito.when(repository.deleteAllHistorical()).thenReturn(Mono.empty());
        Mockito.when(repository.saveAll(Mockito.eq(allCombinations))).thenReturn(Flux.fromIterable(allCombinations));
        StepVerifier.create(exchangeRateService.updateExchangeRates())
                .verifyComplete();

        Mockito.verify(repository).saveAll(allCombinations);
    }
}
