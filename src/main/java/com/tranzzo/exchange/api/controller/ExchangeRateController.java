package com.tranzzo.exchange.api.controller;

import com.tranzzo.exchange.api.ExchangeRateApi;
import com.tranzzo.exchange.api.dto.ExchangeRateDto;
import com.tranzzo.exchange.api.controller.mapper.ExchangeRateMapper;
import com.tranzzo.exchange.domain.CurrencyCode;
import com.tranzzo.exchange.service.ExchangeRateService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class ExchangeRateController implements ExchangeRateApi {

    ExchangeRateService exchangeRateService;

    ExchangeRateMapper exchangeRateMapper;

    @Override
    public Flux<ExchangeRateDto> getExchangeRates(String from, String to) {
        if (from != null && to != null) {
            return exchangeRateService.findAll(CurrencyCode.parse(from), CurrencyCode.parse(to))
                    .flux()
                    .map(exchangeRateMapper::toDto);
        }

        if (from != null) {
            return exchangeRateService.findAll(CurrencyCode.parse(from))
                    .map(exchangeRateMapper::toDto);
        }

        return exchangeRateService.findAll()
                .map(exchangeRateMapper::toDto);
    }

}
