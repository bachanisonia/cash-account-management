package com.jpminterview.repository;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.jpminterview.dto.AccountInput;
import com.jpminterview.entity.Account;

@Repository
public class AccountRepositoryImpl implements AccountRepository {

	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;


	@Override
	public Account getAccount(String accountId) {
		
		Account account = new Account();
		account.setAccountId(accountId);
		Account resultAccount;
		
		try {
			String sql = "select * from account where account_id = :accountId";
		
			resultAccount = jdbcTemplate.queryForObject(sql, new BeanPropertySqlParameterSource(account), new AccountRowMapper());
		}
		catch(EmptyResultDataAccessException e1) {
			return null;
		}
		catch(DataAccessException e2) {
			return null;
		}
		
		return resultAccount;
	}


	@Override
	public int updateAccount(Account account) {
		
		int result;
		
		try {
			String sql = "update account set account_balance = :accountBalance, account_credit_limit = :accountCreditLimit " +
			             " where account_id = :accountId";
			
			Map<String, Object> parameters = new HashMap<>();
			parameters.put("accountBalance", account.getAccountBalance());
			parameters.put("accountCreditLimit", account.getAccountCreditLimit());
			parameters.put("accountId", account.getAccountId());
			
			result = jdbcTemplate.update(sql, parameters);
		}
		catch(DataAccessException  e1) {
			return 0;
		}
		return result;
	}
	
	
	
}
