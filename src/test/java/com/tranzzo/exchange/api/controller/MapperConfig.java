package com.tranzzo.exchange.api.controller;

import com.tranzzo.exchange.api.controller.mapper.ExchangeRateMapper;
import org.mapstruct.factory.Mappers;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class MapperConfig {


    @Bean
    public ExchangeRateMapper exchangeRateMapper() {
        return Mappers.getMapper(ExchangeRateMapper.class);
    }
}
