package com.jpminterview.service;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
	private static final Logger logger = LoggerFactory.getLogger(CashAccountService.class);

	public CashAccountService(AccountRepositoryImpl accountRepository, TransactionRepositoryImpl transactionRepository) {
		this.accountRepository = accountRepository;
		this.transactionRepository = transactionRepository;
	}

	public AccountResponse getAccountDetails(String accountId) {
		
		Account account = accountRepository.getAccount(accountId);
		AccountResponse accountResponse = new AccountResponse();
		
		if (account == null) {
			accountResponse.setMessage(Message.ACCOUNT_NOT_FOUND);
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
		
		logger.info("Current Balance : [{}] , Credit Limit : [{}], Threshold : [{}], Transaction Amount [{}]", currentBalance, creditLimit, threshold, transactionAmount);
		logger.info("Checking if the account has sufficient funds...");
		BigDecimal newBalance = currentBalance.subtract(transactionAmount);
		
		if ( (newBalance.add(creditLimit)).compareTo(threshold) >= 0 ) {
			logger.debug("Account has funds...");
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
			logger.debug("The account does not need any credit...");
			account.setAccountBalance(newBalance);
		}
		else {
			logger.debug("The account may need some credit...");
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
		AccountResponse accountResponse = getAccountDetails(transactionInput.getAccountId());
		Account drAccount = accountResponse.getAccount();
		
		if (drAccount == null) {
			transactionResponse.setMessage(Message.ACCOUNT_NOT_FOUND);
			return transactionResponse;
		}
		
		logger.info("Account[{}] found...", transactionInput.getAccountId());
		
		/* ******  Check Account Funds ******* */
		if ( !hasSufficientFunds(drAccount, transactionAmount) ) {
			transactionResponse.setMessage(Message.INSUFFICIENT_FUNDS);
			return transactionResponse;
		}
		
		logger.info("Account [{}] has sufficient funds for the debit...", transactionInput.getAccountId());
		
		/* ******  Update the account with the new balances ******* */
		updateAccountBalances(drAccount, transactionAmount);
		accountRepository.updateAccount(drAccount);
		
		logger.info("Account [{}] updated with the new balance...", transactionInput.getAccountId());
		
		Transaction drTransaction = new Transaction();
		drTransaction.setAccountId(transactionInput.getAccountId());
		drTransaction.setTransactionAmount(transactionInput.getTransactionAmount());
		drTransaction.setTransactionCurrency(transactionInput.getTransactionCurrency());
		drTransaction.setTransactionType(TransactionType.DEBIT.getTransactionType());
		
		
		/* ******  Save the transaction details ******* */
		transactionRepository.save(drTransaction);
		
		logger.info("New Debit transaction details saved for the account [{}]", transactionInput.getAccountId());
		
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
		AccountResponse accountResponse = getAccountDetails(transactionInput.getAccountId());
		Account crAccount = accountResponse.getAccount();
		
		if (crAccount == null) {
			transactionResponse.setMessage(Message.ACCOUNT_NOT_FOUND);
			return transactionResponse;
		}
		
		logger.info("Account[{}] found...", transactionInput.getAccountId());
		
		/* ******  Update the account with the new balance ******* */
		crAccount.setAccountBalance(crAccount.getAccountBalance().add(transactionAmount));
		accountRepository.updateAccount(crAccount);
		
		logger.info("Account [{}] updated with the new balance...", transactionInput.getAccountId());
		
		Transaction crTransaction = new Transaction();
		crTransaction.setAccountId(transactionInput.getAccountId());
		crTransaction.setTransactionAmount(transactionInput.getTransactionAmount());
		crTransaction.setTransactionCurrency(transactionInput.getTransactionCurrency());
		crTransaction.setTransactionType(TransactionType.CREDIT.getTransactionType());
		
		/* ******  Save the transaction details ******* */
		transactionRepository.save(crTransaction);
		
		transactionResponse.setMessage(Message.OK);
		transactionResponse.setTransaction(crTransaction);
		
		logger.info("New Credit transaction details saved for the account [{}]", transactionInput.getAccountId());
		
		return transactionResponse;
	
	}

}
