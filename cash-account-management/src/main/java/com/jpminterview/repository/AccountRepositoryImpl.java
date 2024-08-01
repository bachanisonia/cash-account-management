package com.jpminterview.repository;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.jpminterview.entity.Account;
import com.jpminterview.util.AccountCacheImpl;

@Repository
public class AccountRepositoryImpl implements AccountRepository {

	private NamedParameterJdbcTemplate jdbcTemplate;
	private AccountCacheImpl accountCache;
	private static final Logger logger = LoggerFactory.getLogger(AccountRepositoryImpl.class);
	
	public AccountRepositoryImpl(NamedParameterJdbcTemplate jdbcTemplate, AccountCacheImpl accountCache) {
		this.jdbcTemplate = jdbcTemplate;
		this.accountCache = accountCache;
	}


	@Override
	public Account getAccount(String accountId) {
		
		// Check if the given account exists in the Cache
		if (accountCache.accountExists(accountId)) {
			logger.debug("Found the account [{}] in the cache", accountId);
			return accountCache.getAccountDetails(accountId);
		}
		
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
		
		// Adding the account details to the cache for subsequent requests
		logger.debug("Saving the account [{}] to the cache", accountId);
		accountCache.saveAccount(resultAccount);
		
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
		
		// Updating the Cache with the new account details
		accountCache.saveAccount(account);
		logger.debug("Saving the account [{}] to the cache", account.getAccountId());
		
		return result;
	}
	
	
	
}
