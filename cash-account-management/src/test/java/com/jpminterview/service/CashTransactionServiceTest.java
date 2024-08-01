package com.jpminterview.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.jpminterview.entity.Transaction;
import com.jpminterview.entity.TransactionType;
import com.jpminterview.repository.AccountRepositoryImpl;
import com.jpminterview.repository.TransactionRepositoryImpl;

@ExtendWith(MockitoExtension.class)
class CashTransactionServiceTest {
	
	@Mock
	private TransactionRepositoryImpl transactionRepository;

	private CashTransactionService mockTransactionService;
	
	@BeforeEach
	void setup() {
		mockTransactionService = new CashTransactionService(transactionRepository);
	}
	
	@Test
	@DisplayName("CashTransactionService-getAllTransactions")
	void testAllTransactions() {
		List<Transaction> transactions = new ArrayList<>();
		transactions.add(new Transaction(1L, TransactionType.CREDIT, new BigDecimal(100.0), "GBP", "ACCOUNT5285"));
		transactions.add(new Transaction(2L, TransactionType.CREDIT, new BigDecimal(100.0), "GBP", "ACCOUNT5285"));
		transactions.add(new Transaction(3L, TransactionType.DEBIT, new BigDecimal(100.0), "GBP", "ACCOUNT1053"));
		transactions.add(new Transaction(4L, TransactionType.DEBIT, new BigDecimal(100.0), "GBP", "ACCOUNT6559"));
		
		when(transactionRepository.getAllTransactions()).thenReturn(transactions);
		
		List<Transaction> resultTransactions = mockTransactionService.getAllTransactions();
		
		assertEquals(transactions.size(), resultTransactions.size());
		assertEquals(transactions, resultTransactions);
	}
	
	@Test
	@DisplayName("CashTransactionService-getTransactionsForAnAccount")
	void testAllTransactionsForAnAccount() {
		List<Transaction> transactions = new ArrayList<>();
		transactions.add(new Transaction(1L, TransactionType.CREDIT, new BigDecimal(100.0), "GBP", "ACCOUNT5285"));
		transactions.add(new Transaction(2L, TransactionType.CREDIT, new BigDecimal(100.0), "GBP", "ACCOUNT5285"));
		
		when(transactionRepository.getTransactionsForAnAccount("ACCOUNT5285")).thenReturn(transactions);
		
		List<Transaction> resultTransactions = mockTransactionService.getTransactionsForAnAccount("ACCOUNT5285");
		
		assertEquals(transactions.size(), resultTransactions.size());
		assertEquals(transactions, resultTransactions);
		
	}
	
	@Test
	@DisplayName("getAllTransactions - Transactions not found")
	void testTransactionsNotFound() {
		
		List<Transaction> transactions = new ArrayList<>();
		when(transactionRepository.getAllTransactions()).thenReturn(transactions);
		
		List<Transaction> resultTransactions = mockTransactionService.getAllTransactions();
		
		assertEquals(transactions.size(), resultTransactions.size());
		assertEquals(transactions, resultTransactions);
		
	}
	
	@Test
	@DisplayName("getTransactionsForAnAccount - Transactions not found")
	void testTransactionsNotFoundForAnAccount() {
		
		String accountId = "ACCOUNT52850";
		
		List<Transaction> transactions = new ArrayList<>();
		when(transactionRepository.getTransactionsForAnAccount(accountId)).thenReturn(transactions);
		
		List<Transaction> resultTransactions = mockTransactionService.getTransactionsForAnAccount(accountId);
		
		assertEquals(transactions.size(), resultTransactions.size());
		assertEquals(transactions, resultTransactions);
		
	}
	
}
