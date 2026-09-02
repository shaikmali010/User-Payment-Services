package com.stackly.paymentService.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.stackly.paymentService.client.UserClient;
import com.stackly.paymentService.dto.PaymentRequestDto;
import com.stackly.paymentService.dto.PaymentResponseDto;
import com.stackly.paymentService.dto.TransactionRequestDto;
import com.stackly.paymentService.dto.TransactionResponseDto;
import com.stackly.paymentService.dto.UserResponseDto;
import com.stackly.paymentService.exception.BalanceCheckException;
import com.stackly.paymentService.exception.IdempotentNotFoundException;
import com.stackly.paymentService.exception.PaymentNotFoundException;
import com.stackly.paymentService.exception.TransactionNotFoundException;
import com.stackly.paymentService.model.Payment;
import com.stackly.paymentService.model.Transaction;
import com.stackly.paymentService.repository.PaymentRepository;
import com.stackly.paymentService.repository.TransactionRepository;
import com.stackly.paymentService.util.IdempotencyStore;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentService {
	
	private final UserClientService userClientService;
	private final IdempotencyStore idempotencyStore;
	private final UserClient userClient;
	private final PaymentRepository paymentRepository;
	private final TransactionRepository transactionRepository;
	
	private static final Logger log =
	        LoggerFactory.getLogger(PaymentService.class);
	

	public PaymentResponseDto createPayment(PaymentRequestDto request) {
		
		Payment payment = new Payment();
		
		payment.setUserId(request.getUserId());
		payment.setAmount(request.getAmount());
		
		Payment savedPayment = paymentRepository.save(payment);
		
		UserResponseDto user = userClientService.getUserById(savedPayment.getUserId());
		
		return PaymentResponseDto.builder()
				.paymentId(savedPayment.getPaymentId())
				.userId(savedPayment.getUserId())
				.amount(savedPayment.getAmount())
				.userName(user.getUserName())
				.email(user.getEmail())
				.build();
	}
	
//	==================================================================
	public PaymentResponseDto getPaymentById(Long paymentId) {
		
		Payment payment = paymentRepository.findById(paymentId)
				.orElseThrow(() -> new PaymentNotFoundException("Payment Not found with id "+paymentId));
		
		UserResponseDto user = userClientService
				.getUserById(payment.getUserId());
		
		return PaymentResponseDto.builder()
				.paymentId(payment.getPaymentId())
				.userId(payment.getUserId())
				.userName(user.getUserName())
				.email(user.getEmail())
				.amount(user.getBalance())
				.build();
	}
	
//	====================================================================
	
	public List<PaymentResponseDto> getPaymentsByUserId(Long userId){
		
		List<Payment> payments = paymentRepository.findByUserId(userId);
		
		return payments.stream().map(payment -> {
			UserResponseDto user = 
					userClientService.getUserById(payment.getUserId());
			
			return PaymentResponseDto.builder()
					.paymentId(payment.getPaymentId())
					.userId(payment.getUserId())
					.amount(payment.getAmount())
					.userName(user.getUserName())
					.email(user.getEmail())
					.build();
		})
				.toList();
				
	}
	
	
//	=====================================================================
	public synchronized TransactionResponseDto transaction(
			TransactionRequestDto request, String idempotencyKey) {
		
		log.info("Starting transaction. Sender: {}, Receiver: {}, Amount: {}",
		        request.getSenderId(),
		        request.getReceiverId(),
		        request.getAmount());
		
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
				
		try {
			
			log.info("Updating sender balance");
			userClient.updateBalance(
					sender.getUserId(), 
					senderBalance);
			
			log.info("Updating receiver balance");
			userClient.updateBalance(
					receiver.getUserId(), 
					receiverBalance);		
			
		}catch(Exception e) {
			
			log.error("Transaction failed. Starting compensation", e);
			
			userClient.updateBalance(
					sender.getUserId(), 
					sender.getBalance());
			
			throw new RuntimeException(
					"Transaction failed. Sender balance restored.", e);
			
		}
		
		// Save successful payment in database
		Payment payment = new Payment();

		payment.setUserId(sender.getUserId());
		payment.setAmount(request.getAmount());

		paymentRepository.save(payment);
		
		log.info("Transaction completed successfully");
		
		// Save transaction in database

		Transaction transaction = new Transaction();

		transaction.setSenderId(sender.getUserId());
		transaction.setReceiverId(receiver.getUserId());
		transaction.setAmount(request.getAmount());
		transaction.setStatus("SUCCESS");

		Transaction savedTransaction = transactionRepository.save(transaction);
		
		TransactionResponseDto response = new TransactionResponseDto(
		        savedTransaction.getTransactionId(),
		        savedTransaction.getSenderId(),
		        savedTransaction.getReceiverId(),
		        savedTransaction.getAmount(),
		        savedTransaction.getStatus()
		);
		

			idempotencyStore.save(idempotencyKey, response);
		
			return response;
	}
	
//	========================================================
	public TransactionResponseDto getTransactionById(Long transactionId) {
		
		Transaction transaction = transactionRepository.findById(transactionId)
				.orElseThrow(() -> new TransactionNotFoundException("Transaction not found with Id "+transactionId));

		return new TransactionResponseDto(
					transaction.getTransactionId(),
					transaction.getSenderId(),
					transaction.getReceiverId(),
					transaction.getAmount(),
					transaction.getStatus());
	}
	
	

}
