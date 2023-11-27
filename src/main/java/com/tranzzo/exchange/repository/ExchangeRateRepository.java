package com.tranzzo.exchange.repository;

import com.tranzzo.exchange.domain.CurrencyCode;
import com.tranzzo.exchange.domain.ExchangeRate;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface ExchangeRateRepository extends R2dbcRepository<ExchangeRate, Long> {

    @Query("""
            SELECT * FROM currency_exchange_rate
            WHERE timestamp = (SELECT MAX(timestamp) FROM currency_exchange_rate)
            """)
    Flux<ExchangeRate> findAllLatest();

    @Query("""
            SELECT * FROM currency_exchange_rate
            WHERE timestamp = (SELECT MAX(timestamp) FROM currency_exchange_rate)
            AND currency_from = :from
            """)
    Flux<ExchangeRate> findAllLatest(@Param("from") CurrencyCode from);

    @Query("""
            SELECT * FROM currency_exchange_rate
            WHERE timestamp = (SELECT MAX(timestamp) FROM currency_exchange_rate)
            AND currency_from = :from
            AND currency_to = :to
            """)
    Mono<ExchangeRate> findAllLatest(@Param("from") CurrencyCode from, @Param("to") CurrencyCode to);


    @Query("""
            DELETE FROM currency_exchange_rate
            WHERE timestamp != (SELECT MAX(timestamp) FROM currency_exchange_rate)
            """)
    Mono<Void> deleteAllHistorical();
}
