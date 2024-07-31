package com.jpminterview.service;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jpminterview.dto.AccountInput;
import com.jpminterview.dto.AccountResponse;
import com.jpminterview.dto.TransactionInput;
import com.jpminterview.dto.TransactionResponse;
import com.jpminterview.entity.Account;
import com.jpminterview.entity.Transaction;
import com.jpminterview.entity.TransactionType;
import com.jpminterview.repository.AccountRepositoryImpl;
import com.jpminterview.repository.TransactionRepositoryImpl;
import com.jpminterview.util.Message;

@Service
public class CashAccountService implements AccountService {

	private AccountRepositoryImpl accountRepository;
	private TransactionRepositoryImpl transactionRepository;
	private static final Logger log = LoggerFactory.getLogger(CashAccountService.class);
	
	@Autowired
	public CashAccountService(AccountRepositoryImpl accountRepository, TransactionRepositoryImpl transactionRepository) {
		this.accountRepository = accountRepository;
		this.transactionRepository = transactionRepository;
	}

	public AccountResponse getAccount(String accountId) {
		
		Account account = accountRepository.getAccount(accountId);
		AccountResponse accountResponse = new AccountResponse();
		
		if (account == null) {
			accountResponse.setMessage(Message.NO_ACCOUNT_FOUND);
		}
		
		accountResponse.setMessage(Message.OK);
		accountResponse.setAccount(account);
		return accountResponse;
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
	@Transactional
	public TransactionResponse debit(TransactionInput transactionInput) {
	
		BigDecimal transactionAmount = transactionInput.getTransactionAmount();
		TransactionResponse transactionResponse = new TransactionResponse();
		
		
		/* ******  Get Account details ******* */
		AccountResponse accountResponse = getAccount(transactionInput.getAccountId());
		Account drAccount = accountResponse.getAccount();
		
		if (drAccount == null) {
			transactionResponse.setMessage(Message.NO_ACCOUNT_FOUND);
			return transactionResponse;
		}
		
		
		/* ******  Check Account Funds ******* */
		if ( !hasSufficientFunds(drAccount, transactionAmount) ) {
			transactionResponse.setMessage(Message.INSUFFICIENT_FUNDS);
			return transactionResponse;
		}
		
		
		/* ******  Update the account with the new balances ******* */
		updateAccountBalances(drAccount, transactionAmount);
		accountRepository.updateAccount(drAccount);
		
		Transaction drTransaction = new Transaction();
		drTransaction.setAccountId(transactionInput.getAccountId());
		drTransaction.setTransactionAmount(transactionInput.getTransactionAmount());
		drTransaction.setTransactionCurrency(transactionInput.getTransactionCurrency());
		drTransaction.setTransactionType(TransactionType.DEBIT.getTransactionType());
		
		
		/* ******  Save the transaction details ******* */
		transactionRepository.save(drTransaction);
		
		transactionResponse.setMessage(Message.OK);
		transactionResponse.setTransaction(drTransaction);
		
		return transactionResponse;
		
	}


	@Override
	@Transactional
	public TransactionResponse credit(TransactionInput transactionInput) {
		
		BigDecimal transactionAmount = transactionInput.getTransactionAmount();
		TransactionResponse transactionResponse = new TransactionResponse();
		
		/* ******  Get Account details ******* */
		AccountResponse accountResponse = getAccount(transactionInput.getAccountId());
		Account crAccount = accountResponse.getAccount();
		
		if (crAccount == null) {
			transactionResponse.setMessage(Message.NO_ACCOUNT_FOUND);
			return transactionResponse;
		}
		
		/* ******  Update the account with the new balance ******* */
		crAccount.setAccountBalance(crAccount.getAccountBalance().add(transactionAmount));
		accountRepository.updateAccount(crAccount);
		
		Transaction crTransaction = new Transaction();
		crTransaction.setAccountId(transactionInput.getAccountId());
		crTransaction.setTransactionAmount(transactionInput.getTransactionAmount());
		crTransaction.setTransactionCurrency(transactionInput.getTransactionCurrency());
		crTransaction.setTransactionType(TransactionType.CREDIT.getTransactionType());
		
		/* ******  Save the transaction details ******* */
		transactionRepository.save(crTransaction);
		
		transactionResponse.setMessage(Message.OK);
		transactionResponse.setTransaction(crTransaction);
		
		return transactionResponse;
	
	}

}
