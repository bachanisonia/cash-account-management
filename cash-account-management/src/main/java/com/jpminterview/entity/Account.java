package com.jpminterview.entity;

import java.math.BigDecimal;
import java.util.Objects;

import org.springframework.stereotype.Component;

@Component
public class Account {

	private String accountId;
	private AccountType accountType;
	private String accountCurrency;
	private BigDecimal accountBalance;
	private final BigDecimal accountThreshold;
	private BigDecimal accountCreditLimit;
	
	public Account() {
		this.accountType = AccountType.CASH_ACCOUNT;
		this.accountThreshold = new BigDecimal(500);
		//this.accountCreditLimit = new BigDecimal(1000);
		this.accountCurrency = "GBP";
	}

	public Account(String accountId, String accountCurrency, BigDecimal accountBalance, BigDecimal accountThreshold,
			BigDecimal accountCreditLimit) {
		
		this.accountType = AccountType.CASH_ACCOUNT;
		this.accountThreshold = new BigDecimal(500);
		//this.accountCreditLimit = new BigDecimal(1000);
		this.accountCurrency = "GBP";
		
		this.accountId = accountId;
		this.accountBalance = accountBalance;

	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public void setAccountType(AccountType accountType) {
		this.accountType = accountType;
	}

	public void setAccountCurrency(String accountCurrency) {
		this.accountCurrency = accountCurrency;
	}

	public void setAccountBalance(BigDecimal accountBalance) {
		this.accountBalance = accountBalance;
	}

	public void setAccountCreditLimit(BigDecimal accountCreditLimit) {
		this.accountCreditLimit = accountCreditLimit;
	}

	public String getAccountId() {
		return accountId;
	}

	public String getAccountCurrency() {
		return accountCurrency;
	}

	public BigDecimal getAccountBalance() {
		return accountBalance;
	}

	public BigDecimal getAccountThreshold() {
		return accountThreshold;
	}

	public BigDecimal getAccountCreditLimit() {
		return accountCreditLimit;
	}

	@Override
	public int hashCode() {
		return Objects.hash(accountId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Account other = (Account) obj;
		return Objects.equals(accountId, other.accountId);
	}
	
	
	
	
	
}
