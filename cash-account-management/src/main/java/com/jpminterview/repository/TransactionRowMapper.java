package com.jpminterview.repository;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.jpminterview.entity.Transaction;

public class TransactionRowMapper implements RowMapper<Transaction> {

	@Override
	public Transaction mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		Transaction transaction = new Transaction();
		
		transaction.setTransactionRef(rs.getLong("transaction_ref"));
		transaction.setAccountId(rs.getString("account_id"));
		transaction.setTransactionAmount(rs.getBigDecimal("transaction_amount"));
		transaction.setTransactionCurrency(rs.getString("transaction_currency"));
		transaction.setTransactionType(rs.getString("transaction_type"));
		
		return transaction;
	}

}
