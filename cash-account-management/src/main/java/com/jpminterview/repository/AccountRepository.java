package com.jpminterview.repository;

import java.util.List;
import java.util.Optional;

import com.jpminterview.dto.AccountInput;
import com.jpminterview.entity.Account;

public interface AccountRepository {

	public Account getAccount(String accountId);
	public int updateAccount(Account account);
}
