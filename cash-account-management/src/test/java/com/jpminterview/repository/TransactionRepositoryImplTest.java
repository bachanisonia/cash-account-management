package com.jpminterview.repository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.jpminterview.entity.Account;
import com.jpminterview.entity.Transaction;
import com.jpminterview.entity.TransactionType;
import com.jpminterview.util.AccountCacheImpl;

@ExtendWith(MockitoExtension.class)
class TransactionRepositoryImplTest {

	@Mock
	private NamedParameterJdbcTemplate jdbcTemplate;
	
	@Mock
	private AccountCacheImpl accountCache;
	
	private TransactionRepositoryImpl mockTransactionRepository;
	
	@BeforeEach
	void setup() {
		mockTransactionRepository = new TransactionRepositoryImpl(jdbcTemplate, accountCache);
	}
	
	@Nested
	@DisplayName("getAllTransactions")
	class TestAllTransactions {
		
		@Test
		@DisplayName("getAllTransactions - Transactions not found")
		void testTransactionsNotFound() {
			
			Mockito.when(jdbcTemplate.query(anyString(), any(TransactionRowMapper.class))).thenReturn(null);
			List<Transaction> resultTransactions = mockTransactionRepository.getAllTransactions();
			
			assertNull(resultTransactions);
		}
		
		@Test
		@DisplayName("getAllTransactions - Transactions found")
		void testTransactionsFound() {
			
			String accountId = "ACCOUNT5285";
			List<Transaction> transactions = new ArrayList<>();
			transactions.add(new Transaction(1L, TransactionType.CREDIT, new BigDecimal(100.0), "GBP", accountId));
			transactions.add(new Transaction(2L, TransactionType.CREDIT, new BigDecimal(100.0), "GBP", accountId));
			
			Mockito.when(jdbcTemplate.query(anyString(), any(TransactionRowMapper.class))).thenReturn(transactions);
			List<Transaction> resultTransactions = mockTransactionRepository.getAllTransactions();
			
			assertEquals(resultTransactions.size(), 2);
		}
	}
	
	@Nested
	@DisplayName("getTransactionsForAnAccount")
	class TestTransactionsForAccount {
	
		@Test
		@DisplayName("getTransactionsForAnAccount - Account not found")
		void testForAccountNotFound() {
			String accountId = "ACCOUNT52850";
			
			Mockito.when(jdbcTemplate.query(anyString(), any(MapSqlParameterSource.class), any(TransactionRowMapper.class))).thenReturn(null);
			
			List<Transaction> resultTransactions = mockTransactionRepository.getTransactionsForAnAccount(accountId);
			
			assertNull(resultTransactions);
			
		}
			
		@Test
		@DisplayName("getTransactionsForAnAccount - Account found")
		void testForAccountFound() {
			String accountId = "ACCOUNT5285";
			List<Transaction> transactions = new ArrayList<>();
			transactions.add(new Transaction(1L, TransactionType.CREDIT, new BigDecimal(100.0), "GBP", accountId));
			transactions.add(new Transaction(2L, TransactionType.CREDIT, new BigDecimal(100.0), "GBP", accountId));
			
			Mockito.when(jdbcTemplate.query(anyString(), any(MapSqlParameterSource.class), any(TransactionRowMapper.class))).thenReturn(transactions);
			
			List<Transaction> resultTransactions = mockTransactionRepository.getTransactionsForAnAccount(accountId);

			assertEquals(resultTransactions.size(), 2);
			
		}
		
		@Test
		@DisplayName("getTransactionsForAnAccount - Account found in cache")
		void testForAccountFoundInCache() {
			String accountId = "ACCOUNT5285";
			List<Transaction> transactions = new ArrayList<>();
			transactions.add(new Transaction(1L, TransactionType.CREDIT, new BigDecimal(100.0), "GBP", accountId));
			transactions.add(new Transaction(2L, TransactionType.CREDIT, new BigDecimal(100.0), "GBP", accountId));
			
			when(accountCache.transactionExists(accountId)).thenReturn(true);
			when(accountCache.getAccountTransactions(accountId)).thenReturn(transactions);
			
			List<Transaction> resultTransactions = mockTransactionRepository.getTransactionsForAnAccount(accountId);
			
			assertEquals(resultTransactions.size(), 2);
			
		}
	}

}
