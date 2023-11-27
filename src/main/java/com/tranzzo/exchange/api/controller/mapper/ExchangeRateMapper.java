package com.tranzzo.exchange.api.controller.mapper;

import com.tranzzo.exchange.api.dto.ExchangeRateDto;
import com.tranzzo.exchange.domain.ExchangeRate;
import org.mapstruct.Mapper;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface ExchangeRateMapper {
    ExchangeRateDto toDto(ExchangeRate exchangeRate);

}
