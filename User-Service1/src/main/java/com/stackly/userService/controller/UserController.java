package com.stackly.userService.controller;

import java.math.BigDecimal;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.stackly.userService.dto.UserRequestDto;
import com.stackly.userService.dto.UserResponseDto;
import com.stackly.userService.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Validated
public class UserController {
	
	private final UserService userService;
	
	@PostMapping
	public UserResponseDto createUser(@Valid @RequestBody UserRequestDto request) {
		return userService.createUser(request);
	}
	
	@GetMapping("/{userId}")
	public UserResponseDto getUserById(@PathVariable("userId") Long userId)  throws InterruptedException{
//		Thread.sleep(5000);
		return userService.getUserById(userId);
	}
	
	@PutMapping("/{userId}/balance")
	public String updateBalance(@PathVariable("userId") Long userId, @RequestParam("balance") BigDecimal balance) {
		userService.updateBalnce(userId, balance);
		return "User Updated successfully!";
	}

}
