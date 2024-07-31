package com.jpminterview.service;

import org.springframework.http.ResponseEntity;

import com.jpminterview.dto.AccountInput;
import com.jpminterview.dto.AccountResponse;
import com.jpminterview.dto.TransactionInput;
import com.jpminterview.dto.TransactionResponse;
import com.jpminterview.entity.Account;
import com.jpminterview.entity.Transaction;

public interface AccountService {

	public AccountResponse getAccountDetails(String accountId);
	public TransactionResponse credit(TransactionInput transactionInput);
	public TransactionResponse debit(TransactionInput transactionInput);
}
