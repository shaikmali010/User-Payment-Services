package com.stackly.userService.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequestDto {
	
	private Long userId;
	
	@NotBlank(message = "User name must not empty")
	private String userName;
	
	@NotBlank(message = "Email is Must not empty")
	@Email(message = "Email id is Invalid")
	private String email;
	
	@DecimalMin(
			value = "500.0",
			message = "The Minimum Balance should be more Than 500 rupees ")
	private BigDecimal balance;
	
	

}
