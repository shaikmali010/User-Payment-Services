package com.stackly.paymentService.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionRequestDto {
	
	@NotNull(message = "Sender ID is required")
	private Long senderId;
	
	@NotNull(message = "Receiver ID is required")
	private Long receiverId;
	
	@NotNull(message = "Amount is required")
	@DecimalMin(
			value = "0.01",
			message = "Amount must be greater than zero"
			)
	private BigDecimal amount;
	

}
