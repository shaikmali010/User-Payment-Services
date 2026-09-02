package com.stackly.gatewayService.filter;

import java.util.UUID;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import reactor.core.publisher.Mono;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CorrelationIdFilter implements WebFilter{
	
	public static final String CORRELATION_ID = "X-Correlation-ID";
	
	@Override
	public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain){
		
		String correlationId = 
				exchange.getRequest()
						.getHeaders()
						.getFirst(CORRELATION_ID);
		
		if(correlationId == null || correlationId.isBlank()) {
			
			correlationId = UUID.randomUUID().toString();
		}
		
		exchange.getResponse()
		.getHeaders()
		.set(CORRELATION_ID, correlationId);
		
		return chain.filter(
				exchange.mutate()
						.request(exchange.getRequest()
								.mutate()
								.header(CORRELATION_ID, correlationId)
								.build())
						.build());
	}
	
	

}
