package com.jpminterview.controller;

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

import com.jpminterview.dto.AccountTransactionInput;
import com.jpminterview.entity.Account;
import com.jpminterview.entity.Transaction;
import com.jpminterview.service.AccountService;
import com.jpminterview.util.Message;

@RestController
@RequestMapping("/cash-management")
public class AccountController {

	private AccountService accountService;
	
	@Autowired
	public AccountController(AccountService accountService) {
		this.accountService = accountService;
	}

	@PostMapping("/accounts")
	public ResponseEntity<?> getAccountInfo(@RequestParam String accountId) {
		
		Account account = accountService.getAccount(accountId);
		
		if (account == null) {
			return new ResponseEntity<>(Message.NO_ACCOUNT_FOUND, HttpStatus.OK);
		}
		else {
			return new ResponseEntity<>(account, HttpStatus.OK);
		}
	}
	
	@PostMapping("/debit")
	public ResponseEntity<?> accountDebit(@RequestBody AccountTransactionInput accountTransactionInput) {
		return accountService.debit(accountTransactionInput);
	}
	
	@PostMapping("/credit")
	public ResponseEntity<?> accountCredit(@RequestBody AccountTransactionInput accountTransactionInput) {
		return accountService.credit(accountTransactionInput);
	}
	
	
}
