package com.jpminterview.service;

import com.jpminterview.dto.AccountResponse;
import com.jpminterview.dto.TransactionInput;
import com.jpminterview.dto.TransactionResponse;

public interface AccountService {

	public AccountResponse getAccountDetails(String accountId);
	public TransactionResponse credit(TransactionInput transactionInput);
	public TransactionResponse debit(TransactionInput transactionInput);
}
