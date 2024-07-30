package com.jpminterview.service;

import java.util.List;

import com.jpminterview.entity.Transaction;

public interface TransactionService {

	public List<Transaction> getAllTransactions();
	public List<Transaction> getTransactionsForAnAccount(String accountId);
}
