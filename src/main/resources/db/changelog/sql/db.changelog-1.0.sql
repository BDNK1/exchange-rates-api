--liquibase formatted sql

--changeset budniak:1700917180
CREATE TABLE currency_exchange_rate
(
    currency_from VARCHAR         NOT NULL,
    currency_to   VARCHAR         NOT NULL,
    rate          DECIMAL(20, 10) NOT NULL,
    timestamp     BIGINT          NOT NULL,

    PRIMARY KEY (currency_from, currency_to, timestamp)
);


