package com.stackly.authenticationService.service;

import org.springframework.stereotype.Service;

import com.stackly.authenticationService.dto.LoginRequestDto;
import com.stackly.authenticationService.dto.LoginResponseDto;
import com.stackly.authenticationService.model.AuthUser;
import com.stackly.authenticationService.repository.AuthUserRepository;
import com.stackly.authenticationService.security.JwtService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService{
	
	private final AuthUserRepository authUserRepository;
	
	private final JwtService jwtService;
	
	@Override
	public LoginResponseDto login(LoginRequestDto request) {
		
		AuthUser user = 
				authUserRepository.findByUsername(request.getUsername())
							.orElseThrow(() -> new RuntimeException("Invalid username or password"));
		
		if(!user.getPassword().equals(request.getPassword())) {
			throw new RuntimeException("Invalid username or password");
		}
		
		String token = jwtService.generateToken(
				user.getUsername(), 
				user.getRole());
		
		return new  LoginResponseDto(token);
	}

}
