package com.stackly.paymentService.exception;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ErrorResponse {
	
	private LocalDateTime timestamp;
	
	private int status;
	
	private String error;
	
	private String message;
	
	private String path;

}
