package com.stackly.paymentService.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import feign.RequestInterceptor;

@Configuration
public class FeignCorrelationIdConfig {

    private static final String CORRELATION_ID =
            "X-Correlation-ID";

    @Bean
    public RequestInterceptor correlationIdInterceptor() {

        return requestTemplate -> {

            String correlationId =
                    org.slf4j.MDC.get(CORRELATION_ID);

            if (correlationId != null) {

                requestTemplate.header(
                        CORRELATION_ID,
                        correlationId
                );
            }
        };
    }
}