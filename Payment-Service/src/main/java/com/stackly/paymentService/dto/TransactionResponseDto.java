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
public class TransactionResponseDto {
	
    private Long transactionId;
	
	private Long senderId;
	
	private Long receiverId;
	
	private BigDecimal amount;
	
	private String status;
	

}
