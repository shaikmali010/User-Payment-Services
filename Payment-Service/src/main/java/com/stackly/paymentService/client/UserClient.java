package com.stackly.paymentService.client;

import java.math.BigDecimal;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.stackly.paymentService.dto.UserResponseDto;

@FeignClient(
		name = "User-Service1")
public interface UserClient {
	
	@GetMapping("/users/{userId}")
	UserResponseDto getUserById(@PathVariable("userId") Long userId);
	
	@PutMapping("/users/{userId}/balance")
	String updateBalance(
			@PathVariable("userId") Long userId,
			@RequestParam("balance") BigDecimal balance);

}
