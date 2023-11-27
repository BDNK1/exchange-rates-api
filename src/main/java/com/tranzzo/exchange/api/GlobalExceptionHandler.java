package com.tranzzo.exchange.api;

import com.tranzzo.exchange.domain.error.UnsupportedCurrencyException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UnsupportedCurrencyException.class)
    public Mono<ResponseEntity<String>> handleUnsupportedCurrency(UnsupportedCurrencyException e) {
        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage()));
    }
   @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<String>> handleAnyException(Exception e) {
        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Something went wrong"));
    }

}
