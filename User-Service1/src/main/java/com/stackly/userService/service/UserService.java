package com.stackly.userService.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.stackly.userService.dto.UserResponseDto;
import com.stackly.userService.exception.UserNotFoundException;
import com.stackly.userService.model.User;

@Service
public class UserService {
	
	private final Map<Long, User> users = new HashMap<>();
	
	public UserService() {
		users.put(1L, new User(1L, "Shaik", "shaik12@gmail.com", new BigDecimal(10000)));
		users.put(2L, new User(2L, "mulla", "mulla12@gmail.com", new BigDecimal(15000)));
		users.put(3L, new User(3L, "syed", "syed12@gmail.com", new BigDecimal(20000)));
		
	}
	
	public UserResponseDto getUserById(Long userId) {
		
		User user = users.get(userId);
		
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
		
		User user = users.get(userId);
		
		if(user == null) {
			throw new UserNotFoundException("User not found with id "+userId);
		}
		
		user.setBalance(newBalance);
		
		return "User balance updated successfuly";
	}

}
