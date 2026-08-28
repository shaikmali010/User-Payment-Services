package com.stackly.paymentService.service;

import java.math.BigDecimal;

import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import com.stackly.paymentService.client.UserClient;
import com.stackly.paymentService.dto.UserResponseDto;
import com.stackly.paymentService.exception.UserServiceUnavailableException;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserClientService {
	
	private final UserClient userClient;
	
	@Bulkhead(
			name = "userService",
			type = Bulkhead.Type.SEMAPHORE)
	
	@CircuitBreaker(name = "userService",
					fallbackMethod ="userServiceFallback")
	@Retryable(
			retryFor = Exception.class,
			maxAttempts = 3,
			backoff = @Backoff(delay = 1000))
	public UserResponseDto getUserById(Long userId) {
		
		System.out.println("Calling User Service for user: "+userId);
		
		return userClient.getUserById(userId);
	}
	
	public UserResponseDto userServiceFallback(Long userId, Exception exception) {
		
		 System.out.println("Circuit breaker fallback for user: "+userId);
		 
		 throw new UserServiceUnavailableException("User Service is currently unavailable",
				 exception);
	}
	
	@Recover
	public void recover(Exception exception,
						Long userId,
						BigDecimal balance) {
		
		System.out.println("User Service failed after retries for user: "+userId);
		
		throw new RuntimeException("Unable to update user balance after retries", exception);
	}

}
