package com.jpminterview.repository;

import java.util.List;
import com.jpminterview.entity.Transaction;


public interface TransactionRepository {

	public int save(Transaction transaction);
	public List<Transaction> getAllTransactions();
	public List<Transaction> getTransactionsForAnAccount(String accountId);
}
