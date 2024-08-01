package com.jpminterview.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jpminterview.dto.AccountInput;
import com.jpminterview.entity.Transaction;
import com.jpminterview.service.CashTransactionService;
import com.jpminterview.util.Message;

@RestController
@RequestMapping("/cash-management")
public class TransactionController {

	private static final Logger logger = LoggerFactory.getLogger(TransactionController.class); 
	
	@Autowired
	private CashTransactionService transactionService;
	

	@GetMapping("/transactions")
	public ResponseEntity<?> getAllTransactions() {
		
		logger.info("Received a request to get all the transaction details...");
		
		List<Transaction> transactions = transactionService.getAllTransactions();
		
		if (transactions.size() != 0) {
			logger.info("Transactions found. Printing details to the user...");
			return new ResponseEntity<>(transactions, HttpStatus.OK);
		}
		else {
			logger.info("No transactions found...");
			return new ResponseEntity<>(Message.NO_TRANSACTIONS_FOUND, HttpStatus.OK);
		}
	}

	
	@PostMapping("/transactions")
	public ResponseEntity<?> getTransactionsForAnAccount(@RequestBody AccountInput accountInput) {
		
		logger.info("Received a request to get the transaction details for the account [{}]", accountInput.getAccountId());
		
		List<Transaction> transactions = transactionService.getTransactionsForAnAccount(accountInput.getAccountId());
		
		if (transactions.size() != 0) {
			logger.info("Transactions found for the account [{}]. Printing details to the user...", accountInput.getAccountId());	
			return new ResponseEntity<>(transactions, HttpStatus.OK);
		}
		else {
			logger.info("No transactions found for the account [{}]", accountInput.getAccountId());
			return new ResponseEntity<>(Message.NO_TRANSACTIONS_FOUND, HttpStatus.OK);
		}
	}
	
}
