package com.stackly.paymentService.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponseDto {

	private Long paymentId;
	
	private Long userId;
	
	private BigDecimal amount;
	
	private String userName;
	
	private String email;
}
