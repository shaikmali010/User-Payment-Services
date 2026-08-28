package com.stackly.paymentService.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import com.stackly.paymentService.dto.TransactionResponseDto;

@Component
public class IdempotencyStore {
	
	private final Map<String, TransactionResponseDto> store = 
			new ConcurrentHashMap<>();
	
	public TransactionResponseDto get(String key) {
		return store.get(key);
	}
	
	public void save(String key, TransactionResponseDto response) {
		store.put(key, response);
	}
	
	public boolean contains(String key) {
		return store.containsKey(key);
	}

}
