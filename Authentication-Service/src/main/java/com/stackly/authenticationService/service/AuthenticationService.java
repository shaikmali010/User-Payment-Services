package com.stackly.authenticationService.service;

import com.stackly.authenticationService.dto.LoginRequestDto;
import com.stackly.authenticationService.dto.LoginResponseDto;

public interface AuthenticationService {
	
	LoginResponseDto login(LoginRequestDto request);

}
