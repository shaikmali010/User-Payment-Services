package com.stackly.paymentService.filter;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CorrelationIdFilter implements Filter {

    private static final Logger log =
            LoggerFactory.getLogger(CorrelationIdFilter.class);

    private static final String CORRELATION_ID =
            "X-Correlation-ID";

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest =
                (HttpServletRequest) request;

        HttpServletResponse httpResponse =
                (HttpServletResponse) response;

        String correlationId =
                httpRequest.getHeader(CORRELATION_ID);
        
        // Store ID for the current request
        MDC.put(CORRELATION_ID, correlationId);

        log.info("Correlation ID: {}", correlationId);

        httpResponse.setHeader(
                CORRELATION_ID,
                correlationId
        );

        try {

            chain.doFilter(request, response);

        } finally {

            // Important: clear MDC after request
            MDC.remove(CORRELATION_ID);
        }
    }
}