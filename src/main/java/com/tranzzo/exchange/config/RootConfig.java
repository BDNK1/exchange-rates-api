package com.tranzzo.exchange.config;

import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@ConfigurationPropertiesScan("com.tranzzo.exchange.properties")
@EnableScheduling
public class RootConfig {
}
