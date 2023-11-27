package com.tranzzo.exchange.domain.error;

public class UnsupportedCurrencyException extends RuntimeException {
    public UnsupportedCurrencyException(String value, Throwable cause) {
        super("Unsupported currency: " + value, cause);
    }
}
