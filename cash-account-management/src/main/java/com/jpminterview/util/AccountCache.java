package com.jpminterview.util;

import java.util.List;

import com.jpminterview.entity.Account;
import com.jpminterview.entity.Transaction;

public interface AccountCache {

	public void saveAccount(Account account);
	public void saveTransaction(Transaction transaction);
	public boolean accountExists(String accountId);
	public boolean transactionExists(String accountId);
	public Account getAccountDetails(String accountId);
	public List<Transaction> getAccountTransactions(String accountId);
	
}
