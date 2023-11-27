package com.tranzzo.exchange.service;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import static java.util.concurrent.TimeUnit.MINUTES;

@Service
@Slf4j
@Profile("!test")
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class SchedulingService {

    ExchangeRateService exchangeRateService;

    @Scheduled(fixedRate = 30, initialDelay = 0, timeUnit = MINUTES)
    public void updateExchangeRates() {
        log.info("Updating exchange rates");
        exchangeRateService.updateExchangeRates().subscribe();
    }
}
