package com.stackly.paymentService.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.stackly.paymentService.client.UserClient;
import com.stackly.paymentService.dto.PaymentResponseDto;
import com.stackly.paymentService.dto.TransactionRequestDto;
import com.stackly.paymentService.dto.TransactionResponseDto;
import com.stackly.paymentService.dto.UserResponseDto;
import com.stackly.paymentService.exception.BalanceCheckException;
import com.stackly.paymentService.exception.IdempotentNotFoundException;
import com.stackly.paymentService.exception.PaymentNotFoundException;
import com.stackly.paymentService.exception.TransactionNotFoundException;
import com.stackly.paymentService.model.Payment;
import com.stackly.paymentService.util.IdempotencyStore;

@Service
public class PaymentService {
	
	private final UserClientService userClientService;
	private final IdempotencyStore idempotencyStore;
	private final UserClient userClient;
	
	private final Map<Long, Payment> payments = new HashMap<>();
	private final Map<Long, TransactionResponseDto> transactions = new HashMap<>();
	
	private Long transactionSequence = 0L;
	
	
//	========================================================================================
	public PaymentService(UserClientService userClientService, IdempotencyStore idempotencyStore, UserClient userClient) {
		
		this.userClientService = userClientService;
		this.idempotencyStore = idempotencyStore;
		this.userClient = userClient;
		
		payments.put(1L, new Payment(1L, 1L, new BigDecimal("5000"), "SUCCESS"));
		payments.put(2L, new Payment(2L, 2L, new BigDecimal("2500"), "SUCCESS"));
		payments.put(3L, new Payment(3L, 1L, new BigDecimal("6000"), "SUCCESS"));
		payments.put(4L, new Payment(4L, 3L, new BigDecimal("3000"), "SUCCESS"));
		
	}
	
//	==================================================================
	public PaymentResponseDto getPaymentById(Long paymentId) {
		
		Payment payment = payments.get(paymentId);
		
		if(payment == null) {
			throw new PaymentNotFoundException("Payment not found with id "+paymentId);
		}
		
		UserResponseDto user = userClientService.getUserById(payment.getUserId());
		
		return new PaymentResponseDto(
				payment.getPaymentId(),
				payment.getUserId(),
				payment.getAmount(),
				user.getUserName(),
				user.getEmail());
	}
	
//	====================================================================
	
	public List<PaymentResponseDto> getPaymentsByUserId(Long userId){
		return payments.values()
				.stream()
				.filter(payment -> payment.getUserId().equals(userId))
				.map(payment -> new PaymentResponseDto(
						payment.getPaymentId(),
						payment.getUserId(),
						payment.getAmount(),
						null,
						null
						))
				.toList();
	}
	
	
//	=====================================================================
	public synchronized TransactionResponseDto transaction(
			TransactionRequestDto request, String idempotencyKey) {
		
		if(idempotencyKey == null || idempotencyKey.isBlank()) {
			throw new IdempotentNotFoundException("Idempotency-Key is required");
		}
		
		TransactionResponseDto existingTransaction = idempotencyStore.get(idempotencyKey);
		
		if(existingTransaction != null) {
			return existingTransaction;
		}
		
//		Get Sender details
		UserResponseDto sender = userClientService.getUserById(request.getSenderId());
		
//		Get Receiver Details
		UserResponseDto receiver = userClientService.getUserById(request.getReceiverId());
		
		System.out.println("Sender ID: " + sender.getUserId());
	    System.out.println("Receiver ID: " + receiver.getUserId());
	    System.out.println("Sender Balance: " + sender.getBalance());
	    System.out.println("Receiver Balance: " + receiver.getBalance());
		
//		Validate amount
		if(request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
			throw new BalanceCheckException("Amount must be greater than zero");
		}
		
//		check sender balance
		if(sender.getBalance().compareTo(request.getAmount()) < 0) {
			throw new BalanceCheckException("Insufficient balance");
		}
		
//		Calculate new balance
		BigDecimal senderBalance = sender.getBalance().subtract(request.getAmount());
		
		BigDecimal receiverBalance = receiver.getBalance().add(request.getAmount());
				
//		Update sender
		userClient.updateBalance(sender.getUserId(), senderBalance);
		
//		Update receiver balance
		userClient.updateBalance(receiver.getUserId(), receiverBalance);
		
		Long transactionId = ++transactionSequence;
		
		TransactionResponseDto transaction = new TransactionResponseDto(
				transactionId,
				sender.getUserId(),
				receiver.getUserId(),
				request.getAmount(),
				"SUCCESS");
		
		transactions.put(transaction.getTransactionId(), transaction);
		
		idempotencyStore.save(idempotencyKey, transaction);
		
	  return transaction;
		
	}
	
//	========================================================
	public TransactionResponseDto getTransactionById(Long transactionId) {
		
		TransactionResponseDto transaction = transactions.get(transactionId);
		
		if(transaction == null) {
			throw new TransactionNotFoundException("Transaction not found with id "+transactionId);
		}
		return transaction;
	}
	
	

}
