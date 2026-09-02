package com.stackly.authenticationService.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stackly.authenticationService.dto.LoginRequestDto;
import com.stackly.authenticationService.dto.LoginResponseDto;
import com.stackly.authenticationService.service.AuthenticationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
	
	private final AuthenticationService authenticationService;

	
	@PostMapping("/login")
	public ResponseEntity<LoginResponseDto> login(
			@RequestBody LoginRequestDto request){
		
		LoginResponseDto response = 
				authenticationService.login(request);
		
		return ResponseEntity.ok(response);
	}
}
