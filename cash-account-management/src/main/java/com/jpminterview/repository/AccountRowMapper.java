package com.jpminterview.repository;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.jpminterview.entity.Account;

public class AccountRowMapper implements RowMapper<Account> {

	@Override
	public Account mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		Account account = new Account();
		
		account.setAccountId(rs.getString("account_id"));
		account.setAccountCurrency(rs.getString("account_currency"));
		account.setAccountBalance(rs.getBigDecimal("account_balance"));
		account.setAccountCreditLimit(rs.getBigDecimal("account_credit_limit"));
		
		return account;
	}

	
}
