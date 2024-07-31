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
import com.jpminterview.dto.AccountResponse;
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
		
		AccountResponse resultAccount = mockAccountService.getAccount(accountId);
		
		assertEquals(resultAccount.getAccount().getAccountId(), account.getAccountId());
		assertEquals(resultAccount.getAccount().getAccountCurrency(), account.getAccountCurrency());
		assertEquals(resultAccount.getAccount().getAccountBalance(), account.getAccountBalance());
		
	}
	
	@Test
	@DisplayName("getAccount - Null Account")
	void testifNullIsReturnedForAccountNotFound() {
		
		String accountId = "ACCOUNT52850";
		when(accountRepository.getAccount(accountId)).thenReturn(null);
		
		AccountResponse resultAccount = mockAccountService.getAccount(accountId);
		
		assertThat(resultAccount.getAccount()).isNull();
		
	}
	
	
}
