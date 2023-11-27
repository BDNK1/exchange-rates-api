package com.tranzzo.exchange.service;


import com.tranzzo.exchange.adapter.ExchangeRateApiClient;
import com.tranzzo.exchange.domain.CurrencyCode;
import com.tranzzo.exchange.domain.ExchangeRate;
import com.tranzzo.exchange.repository.ExchangeRateRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class ExchangeRateService {

    ExchangeRateRepository repository;
    ExchangeRateApiClient apiClient;

    public Flux<ExchangeRate> findAll() {
        return repository.findAllLatest();
    }

    public Flux<ExchangeRate> findAll(CurrencyCode from) {
        return repository.findAllLatest(from);
    }

    public Mono<ExchangeRate> findAll(CurrencyCode from, CurrencyCode to) {
        if (from == to) {
            return Mono.just(new ExchangeRate(from, to, BigDecimal.ONE, System.currentTimeMillis()));
        }
        return repository.findAllLatest(from, to);
    }

    public Mono<Void> updateExchangeRates() {
        return apiClient.getLatest()
                .flatMapMany(response -> {
                    CurrencyCode baseCurrency = CurrencyCode.valueOf(response.base());

                    Map<CurrencyCode, BigDecimal> baseRates = response.rates().entrySet().stream()
                            .collect(Collectors.toMap(
                                    key -> CurrencyCode.valueOf(key.getKey()),
                                    Map.Entry::getValue
                            ));
                    Flux<ExchangeRate> providedRates = Flux.fromIterable(baseRates.entrySet())
                            .filter(rate -> rate.getKey() != baseCurrency)
                            .map(rate -> {
                                CurrencyCode toCurrency = rate.getKey();
                                BigDecimal exchangeRate = rate.getValue();
                                return new ExchangeRate(baseCurrency, toCurrency, exchangeRate, response.timestamp());
                            });

                    Flux<ExchangeRate> calculatedRates = generateAllCombinations(baseCurrency, baseRates, response.timestamp());

                    return Flux.concat(providedRates, calculatedRates);
                })
                .collectList()
                .flatMapMany(repository::saveAll)
                .doOnError(e -> log.error("Failed to update exchange rates", e))
                .doOnComplete(() -> log.info("Exchange rates updated"))
                .then(repository.deleteAllHistorical());
    }

    private Flux<ExchangeRate> generateAllCombinations(CurrencyCode baseCurrency, Map<CurrencyCode, BigDecimal> rates, long timestamp) {
        Set<CurrencyCode> currencies = rates.keySet();

        return Flux.fromIterable(currencies)
                .filter(fromCurrency -> fromCurrency != baseCurrency)
                .flatMap(fromCurrency -> {
                    BigDecimal baseRate = rates.get(fromCurrency);
                    BigDecimal inverseBaseRate = BigDecimal.ONE.divide(baseRate, 10, RoundingMode.HALF_UP);
                    ExchangeRate baseExchangeRate = new ExchangeRate(fromCurrency, baseCurrency, inverseBaseRate, timestamp);

                    Flux<ExchangeRate> toRateFlux = Flux.fromIterable(currencies)
                            .filter(toCurrency -> toCurrency != fromCurrency && toCurrency != baseCurrency)
                            .map(toCurrency -> {
                                BigDecimal fromRate = rates.get(toCurrency);
                                BigDecimal rate = inverseBaseRate.multiply(fromRate);
                                return new ExchangeRate(fromCurrency, toCurrency, rate, timestamp);
                            });

                    return Flux.concat(Flux.just(baseExchangeRate), toRateFlux);
                });
    }


}
