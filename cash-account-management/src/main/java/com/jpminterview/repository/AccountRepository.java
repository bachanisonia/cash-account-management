package com.jpminterview.repository;

import com.jpminterview.entity.Account;

public interface AccountRepository {

	public Account getAccount(String accountId);
	public int updateAccount(Account account);
}
