package com.stackly.paymentService.model;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
	
	private Long transactionId;
	
	private Long senderId;
	
	private Long revciverId;
	
	private BigDecimal amount;
	
	private String status;
	

}
