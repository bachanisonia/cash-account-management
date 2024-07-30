package com.jpminterview.repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.jpminterview.dto.AccountTransactionInput;
import com.jpminterview.entity.Transaction;
import com.jpminterview.entity.TransactionType;

@Repository
public class TransactionRepositoryImpl implements TransactionRepository {
	
	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;
	
	
	@Override
	public int save(Transaction transaction) {
		
		KeyHolder keyHolder = new GeneratedKeyHolder();
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		int result;
		
		String sql = " insert into transaction (transaction_type, transaction_amount, transaction_currency, account_id)" +
					 " values( :transactionType, :transactionAmount, :transactionCurrency, :accountId)";
		
	
		parameters.addValue("transactionType", transaction.getTransactionType());
		parameters.addValue("transactionAmount", transaction.getTransactionAmount());
		parameters.addValue("transactionCurrency", transaction.getTransactionCurrency());
		parameters.addValue("accountId", transaction.getAccountId());
		
		result = jdbcTemplate.update(sql, parameters, keyHolder);
		transaction.setTransactionRef((Long) keyHolder.getKey());
		
		return result;
		
	}

	@Override
	public Transaction getTransactionDetails(Long transactionRef) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Transaction> getAllTransactions() {
		
		String sql = "select * from transaction";
		List<Transaction> transactions = new ArrayList<>();
		
		transactions = jdbcTemplate.query(sql, new TransactionRowMapper());
		
		return transactions;
	}

	@Override
	public List<Transaction> getTransactionsForAnAccount(String accountId) {
		
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		
		parameters.addValue("accountId", accountId);
		
		String sql = "select * from transaction where account_id = :accountId";
		return jdbcTemplate.query(sql, parameters, new TransactionRowMapper());
	}

}
