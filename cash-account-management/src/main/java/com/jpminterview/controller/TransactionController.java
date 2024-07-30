package com.jpminterview.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jpminterview.entity.Transaction;
import com.jpminterview.service.CashTransactionService;
import com.jpminterview.util.Message;

@RestController
@RequestMapping("/cash-management")
public class TransactionController {

	@Autowired
	private CashTransactionService transactionService;
	

	@GetMapping("/transactions")
	public ResponseEntity<?> getAllTransactions() {
		
		List<Transaction> transactions = transactionService.getAllTransactions();
		
		if (transactions != null) {
			return new ResponseEntity<>(transactions, HttpStatus.OK);
		}
		else {
			return new ResponseEntity<>(Message.NO_TRANSACTIONS_FOUND, HttpStatus.OK);
		}
	}

	
	@PostMapping("/transactions")
	public ResponseEntity<?> getAllTransactions(@RequestParam String accountId) {
		
		List<Transaction> transactions = transactionService.getTransactionsForAnAccount(accountId);
		
		if (transactions != null) {
			return new ResponseEntity<>(transactions, HttpStatus.OK);
		}
		else {
			return new ResponseEntity<>(Message.NO_TRANSACTIONS_FOUND, HttpStatus.OK);
		}
	}
	
	
}
