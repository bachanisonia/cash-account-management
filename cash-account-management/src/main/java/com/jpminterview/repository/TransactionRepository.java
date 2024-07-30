package com.jpminterview.repository;

import java.math.BigDecimal;
import java.util.List;

import com.jpminterview.dto.AccountTransactionInput;
import com.jpminterview.entity.Transaction;
import com.jpminterview.entity.TransactionType;

public interface TransactionRepository {

	public int save(Transaction transaction);
	public Transaction getTransactionDetails(Long transactionRef);
	public List<Transaction> getAllTransactions();
	public List<Transaction> getTransactionsForAnAccount(String accountId);
}
