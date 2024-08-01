package com.jpminterview.repository;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.jpminterview.entity.Transaction;
import com.jpminterview.util.AccountCacheImpl;

@Repository
public class TransactionRepositoryImpl implements TransactionRepository {
	
	private NamedParameterJdbcTemplate jdbcTemplate;
	private AccountCacheImpl accountCache;
	private static final Logger logger = LoggerFactory.getLogger(TransactionRepositoryImpl.class);
	
	public TransactionRepositoryImpl(NamedParameterJdbcTemplate jdbcTemplate, AccountCacheImpl accountCache) {
		this.jdbcTemplate = jdbcTemplate;
		this.accountCache = accountCache;
	}

	@Override
	public int save(Transaction transaction) {
		
		KeyHolder keyHolder = new GeneratedKeyHolder();
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		int result;
		
		try {
			String sql = " insert into transaction (transaction_type, transaction_amount, transaction_currency, account_id)" +
						 " values( :transactionType, :transactionAmount, :transactionCurrency, :accountId)";
			
		
			parameters.addValue("transactionType", transaction.getTransactionType());
			parameters.addValue("transactionAmount", transaction.getTransactionAmount());
			parameters.addValue("transactionCurrency", transaction.getTransactionCurrency());
			parameters.addValue("accountId", transaction.getAccountId());
			
			result = jdbcTemplate.update(sql, parameters, keyHolder);
			transaction.setTransactionRef((Long) keyHolder.getKey());
		}
		catch(DataAccessException e1) {
			return 0;
		}
		
		// Update the cache with the new transaction details
		accountCache.saveTransaction(transaction);
		logger.debug("Saving the transaction [{}] for the account [{}] to the cache", transaction.getTransactionRef(), transaction.getAccountId());
		
		return result;
		
	}
	

	@Override
	public List<Transaction> getAllTransactions() {
		
		List<Transaction> transactions = new ArrayList<>();
		
		try {
			String sql = "select * from transaction";	
			transactions = jdbcTemplate.query(sql, new TransactionRowMapper());
		}
		catch(EmptyResultDataAccessException e1) {
			return null;
		}
		catch(DataAccessException e2) {
			return null;
		}
		
		return transactions;
	}

	@Override
	public List<Transaction> getTransactionsForAnAccount(String accountId) {
		
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		List<Transaction> transactions = new ArrayList<>();
		
		// Check the cache if the given account transactions are available
		if (accountCache.transactionExists(accountId)) {
			logger.debug("Found the transactions for account [{}] in the cache", accountId);
			return accountCache.getAccountTransactions(accountId);
		}
		
		parameters.addValue("accountId", accountId);
		
		try {
			String sql = "select * from transaction where account_id = :accountId";
			transactions = jdbcTemplate.query(sql, parameters, new TransactionRowMapper());
		}
		catch(EmptyResultDataAccessException e1) {
			return null;
		}
		catch(DataAccessException e2) {
			return null;
		}
		return transactions;
	}

}
