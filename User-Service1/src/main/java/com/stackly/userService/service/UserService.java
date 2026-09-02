package com.stackly.userService.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.stackly.userService.dto.UserRequestDto;
import com.stackly.userService.dto.UserResponseDto;
import com.stackly.userService.exception.UserExistException;
import com.stackly.userService.exception.UserNotFoundException;
import com.stackly.userService.model.User;
import com.stackly.userService.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
	
	private final UserRepository userRepository;
	
	public UserResponseDto createUser(UserRequestDto request) {
		
		if(userRepository.existsByEmail(request.getEmail())) {
			throw new UserExistException("User already Exist with that  mail");
		}
		
		User user = new User();
		
		user.setUserName(request.getUserName());
		user.setEmail(request.getEmail());
		user.setBalance(request.getBalance());
		
		User savedUser = userRepository.save(user);
		
		return UserResponseDto.builder()
				.userId(savedUser.getUserId())
				.userName(savedUser.getUserName())
				.email(savedUser.getEmail())
				.balance(savedUser.getBalance())
				.build();
		
	}
	
	public UserResponseDto getUserById(Long userId) {
		
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new UserNotFoundException("User not found"));
		
		if(user == null) {
			throw new UserNotFoundException("User not found with id "+userId);
		}
		
		return new UserResponseDto(
				user.getUserId(),
				user.getUserName(),
				user.getEmail(),
				user.getBalance());
	}
	
	public String updateBalnce(Long userId, BigDecimal newBalance) {
		
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new UserNotFoundException("User not found"));
		
		if(user == null) {
			throw new UserNotFoundException("User not found with id "+userId);
		}
		
		user.setBalance(newBalance);
		
		return "User balance updated successfuly";
	}

}
