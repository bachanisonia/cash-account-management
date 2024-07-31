package com.jpminterview.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jpminterview.dto.AccountInput;
import com.jpminterview.dto.AccountResponse;
import com.jpminterview.dto.TransactionInput;
import com.jpminterview.dto.TransactionResponse;
import com.jpminterview.entity.Account;
import com.jpminterview.entity.Transaction;
import com.jpminterview.service.CashAccountService;
import com.jpminterview.util.Message;

@RestController
@RequestMapping("/cash-management")
public class AccountController {
	
	private static final Logger logger = LoggerFactory.getLogger(AccountController.class); 

	private CashAccountService accountService;
	
	@Autowired
	public AccountController(CashAccountService accountService) {
		this.accountService = accountService;
	}

	@PostMapping("/accounts")
	public ResponseEntity<?> getAccountInfo(@RequestBody AccountInput accountInput) {
		
		String accountId = accountInput.getAccountId();
		logger.info("Received a request to get account details for : ["+ accountId + "]");
		
		AccountResponse accountResponse = accountService.getAccountDetails(accountId);
		
		if (!accountResponse.getMessage().equals(Message.OK)) {
			logger.info("Account [" + accountId + "] not found");
			return new ResponseEntity<>(accountResponse.getMessage().getMessageDesc(), HttpStatus.BAD_REQUEST);
		}
		else {
			logger.info("Account [" + accountId + "] found. Printing details to the user...");
			return new ResponseEntity<>(accountResponse.getAccount(), HttpStatus.OK);
		}
	}
	
	@PostMapping("/debit")
	public ResponseEntity<?> accountDebit(@RequestBody TransactionInput transactionInput) {
		
		logger.info("Received a debit request for account [{}], amount [{}], currency [{}]"
				,transactionInput.getAccountId()
				,transactionInput.getTransactionAmount()
				,transactionInput.getTransactionCurrency());
		
		TransactionResponse transactionResponse = accountService.debit(transactionInput);
		
		if (!transactionResponse.getMessage().equals(Message.OK)) {
			logger.info("Unsuccessful Debit Transaction - [{}]", transactionResponse.getMessage().getMessageDesc());
			return new ResponseEntity<>(transactionResponse.getMessage().getMessageDesc(), HttpStatus.BAD_REQUEST);
		}
		else {
			logger.info("Debit successful. Printing details to the user...");
			return new ResponseEntity<>(transactionResponse.getTransaction(), HttpStatus.OK);
		}
	}
	
	@PostMapping("/credit")
	public ResponseEntity<?> accountCredit(@RequestBody TransactionInput transactionInput) {
		
		logger.info("Received a credit request for account [{}], amount [{}], currency [{}]"
				,transactionInput.getAccountId()
				,transactionInput.getTransactionAmount()
				,transactionInput.getTransactionCurrency());
		
		TransactionResponse transactionResponse = accountService.credit(transactionInput);
		
		if (!transactionResponse.getMessage().equals(Message.OK)) {
			logger.info("Unsuccessful Credit Transaction - [{}]", transactionResponse.getMessage().getMessageDesc());
			return new ResponseEntity<>(transactionResponse.getMessage().getMessageDesc(), HttpStatus.BAD_REQUEST);
		}
		else {
			logger.info("Credit successful. Printing details to the user...");
			return new ResponseEntity<>(transactionResponse.getTransaction(), HttpStatus.OK);
		}
	}
	
	
}
