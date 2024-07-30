package com.jpminterview.service;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jpminterview.entity.Account;
import com.jpminterview.entity.Transaction;
import com.jpminterview.repository.AccountRepositoryImpl;
import com.jpminterview.repository.TransactionRepositoryImpl;

@Service
public class CashTransactionService implements TransactionService {

	private AccountRepositoryImpl accountRepository;
	private TransactionRepositoryImpl transactionRepository;
	private static final Logger log = LoggerFactory.getLogger(CashAccountService.class);
	
	@Autowired
	public CashTransactionService(AccountRepositoryImpl accountRepository, TransactionRepositoryImpl transactionRepository) {
		this.accountRepository = accountRepository;
		this.transactionRepository = transactionRepository;
	}

	public List<Transaction> getAllTransactions() {
		List<Transaction> transactions = transactionRepository.getAllTransactions();
		return transactions.stream().sorted( (x1,x2) -> x1.getTransactionRef().compareTo(x2.getTransactionRef())).collect(Collectors.toList());	
	}
	
	public List<Transaction> getTransactionsForAnAccount(String accountId) {
		return transactionRepository.getTransactionsForAnAccount(accountId);
	}
	
}
