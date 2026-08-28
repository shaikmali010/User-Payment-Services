package com.stackly.paymentService.controller;

import java.util.List;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.stackly.paymentService.dto.PaymentResponseDto;
import com.stackly.paymentService.dto.TransactionRequestDto;
import com.stackly.paymentService.dto.TransactionResponseDto;
import com.stackly.paymentService.service.PaymentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
@Validated
public class PaymentController {

	private final PaymentService paymentService;
	
	@GetMapping("/{paymentId}")
	public PaymentResponseDto getPaymentById(
			@PathVariable("paymentId") Long paymentId) {
		
		return paymentService.getPaymentById(paymentId);
	}
	
	@GetMapping
	public List<PaymentResponseDto> getPaymentsByUserId(
	        @RequestParam Long userId) {

	    return paymentService.getPaymentsByUserId(userId);
	}
	
	@PostMapping("/transaction")
	public TransactionResponseDto transaction( 
			@Valid @RequestBody TransactionRequestDto request,
			@RequestHeader("Idempotency-Key") String idempotencyKey) {
		
		return paymentService.transaction(request, idempotencyKey);
	}
	
	@GetMapping("/transactions/{transactionId}")
	public TransactionResponseDto getTransactionById(
			@PathVariable("transactionId") Long transactionId) {
		
		return paymentService.getTransactionById(transactionId);
	}
	
	@PostMapping("/test")
	public String test() {
	    return "POST is working";
	}
}
