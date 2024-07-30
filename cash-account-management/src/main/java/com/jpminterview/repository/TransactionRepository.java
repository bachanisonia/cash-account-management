package com.jpminterview.repository;

import java.math.BigDecimal;

import com.jpminterview.dto.AccountTransactionInput;
import com.jpminterview.entity.Transaction;
import com.jpminterview.entity.TransactionType;

public interface TransactionRepository {

	public int save(Transaction transaction);
	
	public Transaction getTransactionDetails(Long transactionRef);
}
