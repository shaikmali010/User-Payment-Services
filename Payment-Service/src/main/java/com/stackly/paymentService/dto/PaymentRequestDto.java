package com.stackly.paymentService.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
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
public class PaymentRequestDto {
	
	@NotNull(message = "User ID should not be empty")
	private Long userId;
	
	@DecimalMin(
			value = "0.01",
			message = "The amount must be greater then zero")
	private BigDecimal amount;

}
