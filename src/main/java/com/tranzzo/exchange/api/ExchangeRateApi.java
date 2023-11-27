package com.tranzzo.exchange.api;

import com.tranzzo.exchange.api.dto.ExchangeRateDto;
import com.tranzzo.exchange.domain.CurrencyCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Flux;

@RequestMapping("/api/exchange-rates")
public interface ExchangeRateApi {

    @GetMapping
    @Operation(summary = "Get exchange rates",
            parameters = {
                    @Parameter(name = "currencyFrom", description = "Currency code from which exchange rate is calculated", schema = @Schema(implementation = CurrencyCode.class)),
                    @Parameter(name = "currencyTo", description = "Currency code to which exchange rate is calculated", schema = @Schema(implementation = CurrencyCode.class))
            })
    Flux<ExchangeRateDto> getExchangeRates(@RequestParam(name = "currencyFrom", required = false) String currencyFrom,
                                           @RequestParam(name = "currencyTo", required = false) String currencyTo);

}
