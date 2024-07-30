package com.jpminterview.service;

import org.springframework.http.ResponseEntity;

import com.jpminterview.dto.AccountTransactionInput;
import com.jpminterview.entity.Transaction;

public interface AccountTransaction {

	public ResponseEntity<?> credit(AccountTransactionInput transactionInput);
	public ResponseEntity<?> debit(AccountTransactionInput transactionInput);
}
