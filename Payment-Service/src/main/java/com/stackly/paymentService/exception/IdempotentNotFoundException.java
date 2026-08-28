package com.stackly.paymentService.exception;

public class IdempotentNotFoundException extends RuntimeException{
	
	public IdempotentNotFoundException(String message) {
		super(message);
	}

}
