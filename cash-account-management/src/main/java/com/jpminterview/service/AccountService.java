package com.jpminterview.service;

import org.springframework.http.ResponseEntity;

import com.jpminterview.dto.AccountInput;
import com.jpminterview.dto.TransactionInput;
import com.jpminterview.entity.Account;

public interface AccountService {

	public Account getAccount(AccountInput accountInput);
	public ResponseEntity<?> credit(TransactionInput transactionInput);
	public ResponseEntity<?> debit(TransactionInput transactionInput);
}
