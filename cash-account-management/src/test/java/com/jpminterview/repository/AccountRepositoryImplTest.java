package com.jpminterview.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.jpminterview.dto.AccountResponse;
import com.jpminterview.entity.Account;
import com.jpminterview.service.CashAccountService;
import com.jpminterview.util.AccountCacheImpl;

@ExtendWith(MockitoExtension.class)
class AccountRepositoryImplTest {

	@Mock
	private NamedParameterJdbcTemplate jdbcTemplate;
	
	@Mock
	private AccountCacheImpl accountCache;
	
	private AccountRepositoryImpl mockAccountRepository;
	
	@BeforeEach
	void setup() {
		mockAccountRepository = new AccountRepositoryImpl(jdbcTemplate, accountCache);
	}
	
	@Nested
	@DisplayName("Test getAccount")
	class testGetAccount {
	
		@Test
		@DisplayName("getAccount - Account not found")
		void testGetAccount() {
			
			String accountId = "ACCOUNT52850";
			String sql = "select * from account where account_id = :accountId";
			
			Account account = new Account();
			account.setAccountId(accountId);
			
			Mockito.when(jdbcTemplate.queryForObject(any(String.class), any(BeanPropertySqlParameterSource.class), Mockito.any(AccountRowMapper.class))).thenReturn(null);
			
			Account resultAccount = mockAccountRepository.getAccount(accountId);
			
			assertNull(resultAccount);
			
		}
		
		@Test
		@DisplayName("getAccount - Account found")
		void testGetAccountForValidAccount() {
			
			String accountId = "ACCOUNT5285";
			Account account = new Account(accountId, "GBP", new BigDecimal(500.0), new BigDecimal(500.0), new BigDecimal(500.0));
			
			String sql = "select * from account where account_id = :accountId";
			
			Mockito.when(jdbcTemplate.queryForObject(any(String.class), any(BeanPropertySqlParameterSource.class), Mockito.any(AccountRowMapper.class))).thenReturn(account);
			
			Account resultAccount = mockAccountRepository.getAccount(accountId);
			
			assertEquals(account, resultAccount);
			
		}
		
		
		@Test
		@DisplayName("getAccount - Account found in Cache")
		void testGetAccountForValidAccountinCache() {
			
			String accountId = "ACCOUNT5285";
			Account account = new Account(accountId, "GBP", new BigDecimal(500.0), new BigDecimal(500.0), new BigDecimal(500.0));
			
			when(accountCache.accountExists(accountId)).thenReturn(true);
			when(accountCache.getAccountDetails(accountId)).thenReturn(account);
			
			Account resultAccount = mockAccountRepository.getAccount(accountId);
			
			assertEquals(account, resultAccount);
			
		}
		
	}
	
	

}
