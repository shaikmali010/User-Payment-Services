package com.stackly.userService.exception;

import java.time.LocalDateTime;
import java.util.Map;

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
				.timeStamp(LocalDateTime.now())
				.status(status.value())
				.error(error)
				.message(message)
				.path(request.getRequestURI())
				.build();
	}

	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFoundException ex, HttpServletRequest request){
	return ResponseEntity.status(HttpStatus.NOT_FOUND)
							.body(ResponseDto(ex.getMessage(), request, HttpStatus.NOT_FOUND, "USER_NOT_FOUND"));
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(
			MethodArgumentNotValidException ex, HttpServletRequest request){
		
		String message = ex.getBindingResult()
						.getFieldError()
						.getDefaultMessage();
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
							.body(ResponseDto(message, request, HttpStatus.BAD_REQUEST, "INVALID_FIELD"));
	}
	
	@ExceptionHandler(UserExistException.class)
	public ResponseEntity<ErrorResponse> handleUserExist(UserExistException ex, HttpServletRequest request){
		
		return ResponseEntity.status(HttpStatus.CONFLICT)
								.body(ResponseDto(ex.getMessage(), request, HttpStatus.CONFLICT, "USER_ALREADY_EXIST"));
	}
			
}
