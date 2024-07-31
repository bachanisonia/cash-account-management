package com.jpminterview.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.junit.jupiter.MockitoExtension;

import com.jpminterview.dto.AccountInput;
import com.jpminterview.dto.AccountResponse;
import com.jpminterview.dto.TransactionInput;
import com.jpminterview.dto.TransactionResponse;
import com.jpminterview.entity.Account;
import com.jpminterview.repository.AccountRepositoryImpl;
import com.jpminterview.repository.TransactionRepositoryImpl;
import com.jpminterview.util.Message;

@ExtendWith(MockitoExtension.class)
class CashAccountServiceTest {

	@Mock
	private AccountRepositoryImpl accountRepository;
	
	@Mock
	private TransactionRepositoryImpl transactionRepository;
	
	public CashAccountService mockAccountService;

	@BeforeEach
	void setup() {
		mockAccountService = new CashAccountService(accountRepository, transactionRepository);
	}
	
	@Test
	@DisplayName("getAccount - Valid Account")
	void testIfCorrectAccountDetailsAreReturned() {
		
		String accountId = "ACCOUNT5285";
		Account account = new Account(accountId, "GBP", new BigDecimal(500.0), new BigDecimal(500.0), new BigDecimal(500.0));
		
		when(accountRepository.getAccount(accountId)).thenReturn(account);
		
		AccountResponse resultAccount = mockAccountService.getAccountDetails(accountId);
		
		assertEquals(resultAccount.getAccount().getAccountId(), account.getAccountId());
		assertEquals(resultAccount.getAccount().getAccountCurrency(), account.getAccountCurrency());
		assertEquals(resultAccount.getAccount().getAccountBalance(), account.getAccountBalance());
		
	}
	
	@Test
	@DisplayName("getAccount - Null Account")
	void testifNullIsReturnedForAccountNotFound() {
		
		String accountId = "ACCOUNT52850";
		when(accountRepository.getAccount(accountId)).thenReturn(null);
		
		AccountResponse resultAccount = mockAccountService.getAccountDetails(accountId);
		
		assertThat(resultAccount.getAccount()).isNull();
		
	}
	
	@Nested
	@DisplayName("AccountService - Debit")
	class AccountDebitTest {
		
		@Test
		@DisplayName("Account not found")
		void testAccountNotFound() {
			
			String accountId = "ACCOUNT52850";
			TransactionInput transactionInput = new TransactionInput(accountId, new BigDecimal(100.0), "GBP");
			
			when(accountRepository.getAccount(accountId)).thenReturn(null);
			
			TransactionResponse resultResponse =  mockAccountService.debit(transactionInput);
			assertThat(resultResponse.getMessage().equals(Message.ACCOUNT_NOT_FOUND));
			
		}
		
		@Test
		@DisplayName("Insufficient Funds")
		void testInsufficientFunds() {
			
			String accountId = "ACCOUNT5285";
			TransactionInput transactionInput = new TransactionInput(accountId, new BigDecimal(1000.0), "GBP");
			Account account = new Account(accountId, "GBP", new BigDecimal(100.0), new BigDecimal(500.0), new BigDecimal(500.0));
			
			when(accountRepository.getAccount(accountId)).thenReturn(account);
	
			TransactionResponse resultResponse =  mockAccountService.debit(transactionInput);
			
			assertThat(resultResponse.getMessage().equals(Message.INSUFFICIENT_FUNDS));
		
		}
		
		@Test
		@DisplayName("Successful Debit")
		void testSuccessfulDebit() {
			
			String accountId = "ACCOUNT5285";
			TransactionInput transactionInput = new TransactionInput(accountId, new BigDecimal(100.0), "GBP");
			Account account = new Account(accountId, "GBP", new BigDecimal(100.0), new BigDecimal(500.0), new BigDecimal(500.0));
			
			when(accountRepository.getAccount(accountId)).thenReturn(account);
			
			TransactionResponse resultResponse =  mockAccountService.debit(transactionInput);
			
			assertThat(resultResponse.getMessage().equals(Message.OK));
			
		}
	}
	
	@Nested
	@DisplayName("AccountService - Credit")
	class AccountCreditTest {
		
		@Test
		@DisplayName("Account not found")
		void testAccountNotFound() {
			
			String accountId = "ACCOUNT52850";
			TransactionInput transactionInput = new TransactionInput(accountId, new BigDecimal(100.0), "GBP");
			
			when(accountRepository.getAccount(accountId)).thenReturn(null);
			
			TransactionResponse resultResponse =  mockAccountService.credit(transactionInput);
			assertThat(resultResponse.getMessage().equals(Message.ACCOUNT_NOT_FOUND));
			
		}
		
		
		@Test
		@DisplayName("Successful Credit")
		void testSuccessfulDebit() {
			
			String accountId = "ACCOUNT5285";
			TransactionInput transactionInput = new TransactionInput(accountId, new BigDecimal(100.0), "GBP");
			Account account = new Account(accountId, "GBP", new BigDecimal(100.0), new BigDecimal(500.0), new BigDecimal(500.0));
			
			when(accountRepository.getAccount(accountId)).thenReturn(account);
			
			TransactionResponse resultResponse =  mockAccountService.credit(transactionInput);
			
			assertThat(resultResponse.getMessage().equals(Message.OK));
			
		}
	}
	
	
	
	
}
