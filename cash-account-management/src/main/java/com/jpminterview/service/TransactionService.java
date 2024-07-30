package com.jpminterview.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jpminterview.entity.Account;
import com.jpminterview.entity.Transaction;
import com.jpminterview.repository.AccountRepositoryImpl;
import com.jpminterview.repository.TransactionRepositoryImpl;

@Service
public class TransactionService {

	private AccountRepositoryImpl accountRepository;
	private TransactionRepositoryImpl transactionRepository;
	private static final Logger log = LoggerFactory.getLogger(AccountService.class);
	
	@Autowired
	public TransactionService(AccountRepositoryImpl accountRepository, TransactionRepositoryImpl transactionRepository) {
		this.accountRepository = accountRepository;
		this.transactionRepository = transactionRepository;
	}

	public List<Transaction> getAllTransactions() {
		List<Transaction> transactions = transactionRepository.getAllTransactions();
		return transactions;	
	}
	
	public List<Transaction> getTransactionsForAnAccount(String accountId) {
		return transactionRepository.getTransactionsForAnAccount(accountId);
	}
	
}
