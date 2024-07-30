package com.jpminterview.service;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.jpminterview.dto.AccountTransactionInput;
import com.jpminterview.entity.Account;
import com.jpminterview.entity.Transaction;
import com.jpminterview.entity.TransactionType;
import com.jpminterview.repository.AccountRepositoryImpl;
import com.jpminterview.repository.TransactionRepositoryImpl;
import com.jpminterview.util.Message;

@Service
public class AccountService implements AccountTransaction {

	private AccountRepositoryImpl accountRepository;
	private TransactionRepositoryImpl transactionRepository;
	private static final Logger log = LoggerFactory.getLogger(AccountService.class);
	
	@Autowired
	public AccountService(AccountRepositoryImpl accountRepository, TransactionRepositoryImpl transactionRepository) {
		this.accountRepository = accountRepository;
		this.transactionRepository = transactionRepository;
	}

	public Account getAccount(String accountId) {
		
		Account account = accountRepository.getAccount(accountId);
		
		return account;
	}

	
	/* CHECK FOR SUFFICIENT FUNDS
	 *  - Checks the new balance against the threshold
	 *  - Checks the credit limit allowed on the account
	 */
	private static boolean hasSufficientFunds(Account account, BigDecimal transactionAmount) {
		
		BigDecimal currentBalance = account.getAccountBalance();
		BigDecimal creditLimit = account.getAccountCreditLimit();
		BigDecimal threshold = account.getAccountThreshold();
		
		BigDecimal newBalance = currentBalance.subtract(transactionAmount);
		
		if ( (newBalance.add(creditLimit)).compareTo(threshold) >= 0) {
			return true;
		}
		
		return false;
		
	}
	
	
	private static void updateAccountBalances(Account account, BigDecimal transactionAmount) {
	
		BigDecimal currentBalance = account.getAccountBalance();
		BigDecimal creditLimit = account.getAccountCreditLimit();
		BigDecimal threshold = account.getAccountThreshold();
		
		BigDecimal newBalance = currentBalance.subtract(transactionAmount);
		
		if ( newBalance.compareTo(threshold) >= 0 ) {
			account.setAccountBalance(newBalance);
		}
		else {
			BigDecimal creditRequired = threshold.subtract(newBalance);
			
			account.setAccountBalance(newBalance.add(creditRequired));
			account.setAccountCreditLimit(creditLimit.subtract(creditRequired));
		}
		
		
	}
	
	
	/* DEBIT ACCOUNT
	 *  - Updates Account Balances
	 *  - Creates transaction details
	 */
	@Override
	public ResponseEntity<?> debit(AccountTransactionInput transactionInput) {
	
		BigDecimal transactionAmount = transactionInput.getTransactionAmount();
		
		
		/* ******  Get Account details ******* */
		Account drAccount = getAccount(transactionInput.getAccountId());
		
		if (drAccount == null) {
			return new ResponseEntity<>(Message.NO_ACCOUNT_FOUND, HttpStatus.OK);
		}
		
		
		/* ******  Check Account Funds ******* */
		if ( !hasSufficientFunds(drAccount, transactionAmount) ) {
			return new ResponseEntity<>(Message.INSUFFICIENT_FUNDS, HttpStatus.BAD_REQUEST);
		}
		
		
		/* ******  Update the account with new balances ******* */
		updateAccountBalances(drAccount, transactionAmount);
		accountRepository.updateAccount(drAccount);
		
		Transaction drTransaction = new Transaction();
		drTransaction.setAccountId(transactionInput.getAccountId());
		drTransaction.setTransactionAmount(transactionInput.getTransactionAmount());
		drTransaction.setTransactionCurrency(transactionInput.getTransactionCurrency());
		drTransaction.setTransactionType(TransactionType.DEBIT.getTransactionType());
		
		
		/* ******  Save the transaction details ******* */
		transactionRepository.save(drTransaction);
		
		return new ResponseEntity<>(drTransaction, HttpStatus.OK);
		
	}


	@Override
	public ResponseEntity<?> credit(AccountTransactionInput transactionInput) {
		Transaction crTransaction = new Transaction();
		return new ResponseEntity<>(crTransaction, HttpStatus.OK);
	}
}
