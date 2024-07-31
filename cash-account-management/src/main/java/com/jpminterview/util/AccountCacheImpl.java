package com.jpminterview.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import com.jpminterview.entity.Account;
import com.jpminterview.entity.Transaction;

@Component
public class AccountCacheImpl implements AccountCache {

	private Map<String, Account> accountMap;
	private Map<String, List<Transaction>> transactionMap;
	
	public AccountCacheImpl() {
		this.accountMap = new ConcurrentHashMap<>();
		this.transactionMap = new ConcurrentHashMap<>();
	}

	@Override
	public Account getAccountDetails(String accountId) {
		return accountMap.get(accountId);
	}

	@Override
	public List<Transaction> getAccountTransactions(String accountId) {
		return transactionMap.get(accountId);
	}

	@Override
	public void saveAccount(Account account) {
		accountMap.put(account.getAccountId(), account);
	}

	@Override
	public void saveTransaction(Transaction transaction) {
		String accountId = transaction.getAccountId();
		
		if (transactionExists(accountId)) {
			List<Transaction> transactions = transactionMap.get(accountId);
			transactions.add(transaction);
			transactionMap.put(accountId, transactions);
		}
		else {
			List<Transaction> transactions = new ArrayList<>();
			transactions.add(transaction);
			transactionMap.put(accountId, transactions);
		}
		
	}

	@Override
	public boolean accountExists(String accountId) {
		return accountMap.containsKey(accountId);
	}

	@Override
	public boolean transactionExists(String accountId) {
		return transactionMap.containsKey(accountId);
	}

}
