package com.tranzzo.exchange.domain;

import com.tranzzo.exchange.domain.error.UnsupportedCurrencyException;

public enum CurrencyCode {
    EUR,
    UAH,
    USD,
    PLN,
    JPY,
    GBP,
    PHP,
    CAD,
    AUD,
    CHF,
    CNY,
    NOK,
    CZK,
    BRL,
    MYR,
    MXN,
    NIO,
    NPR,
    PEN,
    BYR,
    COP,
    MUR,
    MRO,
    MZN,
    SEK,
    DKK;


    public static CurrencyCode parse(String code) {
        try {
            return CurrencyCode.valueOf(code.toUpperCase());
        } catch (Exception e) {
            throw new UnsupportedCurrencyException("Currency code " + code + " is not supported", e);
        }
    }
}
