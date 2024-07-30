package com.jpminterview.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.junit.jupiter.MockitoExtension;

import com.jpminterview.dto.AccountInput;
import com.jpminterview.entity.Account;
import com.jpminterview.repository.AccountRepositoryImpl;
import com.jpminterview.repository.TransactionRepositoryImpl;

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
		
		Account account = new Account("ACCOUNT5285", "GBP", new BigDecimal(500.0), new BigDecimal(500.0), new BigDecimal(500.0));
		
		when(accountRepository.getAccount(new AccountInput("ACCOUNT5285"))).thenReturn(account);
		
		Account resultAccount = mockAccountService.getAccount(new AccountInput("ACCOUNT5285"));
		
		assertEquals(resultAccount.getAccountId(), account.getAccountId());
		assertEquals(resultAccount.getAccountCurrency(), account.getAccountCurrency());
		assertEquals(resultAccount.getAccountBalance(), account.getAccountBalance());
		
	}
	
	@Test
	@DisplayName("getAccount - Null Account")
	void testifNullIsReturnedForAccountNotFound() {
		
		when(accountRepository.getAccount(new AccountInput("ACCOUNT5285"))).thenReturn(null);
		
		Account resultAccount = mockAccountService.getAccount(new AccountInput("ACCOUNT5285"));
		
		assertThat(resultAccount).isNull();
		
	}
	
	
}
