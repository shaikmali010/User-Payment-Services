package com.stackly.paymentService.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	public ErrorResponse ResponseDto(
			String message, 
			HttpServletRequest request, 
			HttpStatus status,
			String error) {
		
		return ErrorResponse.builder()
						.timestamp(LocalDateTime.now())
						.status(status.value())
						.error(error)
						.message(message)
						.path(request.getRequestURI())
						.build();
				}

	@ExceptionHandler(PaymentNotFoundException.class)
	public ResponseEntity<ErrorResponse> handlePaymentNotFound(
			PaymentNotFoundException ex, 
			HttpServletRequest request){
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
									.body(ResponseDto(
										ex.getMessage(), 
										request,
										HttpStatus.NOT_FOUND,
										"PAYMENT_NOT_FOUND"));
	}
	
	@ExceptionHandler(TransactionNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleTransactionNotFound(
			TransactionNotFoundException ex, 
			HttpServletRequest request){
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(ResponseDto(
						ex.getMessage(), 
						request,
						HttpStatus.NOT_FOUND,
						"TRANSACTION_NOT_FOUND"));
	}
	
	@ExceptionHandler(BalanceCheckException.class)
	public ResponseEntity<ErrorResponse> handleBalanceCheck(
			BalanceCheckException ex, 
			HttpServletRequest request){
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(ResponseDto(
						ex.getMessage(),
						request,
						HttpStatus.BAD_REQUEST,
						"CHECK_YOUR_BALANCE"));
	}
	
	@ExceptionHandler(IdempotentNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleIdempotentNotFound(
			IdempotentNotFoundException ex,
			HttpServletRequest request){
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(ResponseDto(
						ex.getMessage(),
						request,
						HttpStatus.NOT_FOUND,
						"IDEMPOTENT_NOT_FOUND"));
	}
	
	@ExceptionHandler(UserServiceUnavailableException.class)
	public ResponseEntity<ErrorResponse> handleUserServiceUnabailable(
			UserServiceUnavailableException ex,
			HttpServletRequest request){
		
		String message = "User Service is currently unavailable. Please try again later.";
		
		return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
				.body(ResponseDto(message, request, HttpStatus.SERVICE_UNAVAILABLE, "SERVICE_UNAVAILABLE"));
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleMethodArgumentNotFound(
						MethodArgumentNotValidException ex,
						HttpServletRequest request){
		
		String message = ex.getBindingResult()
				.getFieldError()
				.getDefaultMessage();
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(ResponseDto(
						message, 
						request, 
						HttpStatus.BAD_REQUEST, 
						"VALIDATION_ERROR"));
		
		
		
	}
}
