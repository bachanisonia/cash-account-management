package com.jpminterview.dto;

import java.math.BigDecimal;
import java.util.Objects;

public class TransactionInput {
	
	String accountId;
	BigDecimal transactionAmount;
	String transactionCurrency;
	
	public TransactionInput() {
	}
	
	public TransactionInput(String accountId, BigDecimal transactionAmount,
			String transactionCurrency) {
		this.accountId = accountId;
		this.transactionAmount = transactionAmount;
		this.transactionCurrency = transactionCurrency;
	}

	public String getAccountId() {
		return accountId;
	}

	public BigDecimal getTransactionAmount() {
		return transactionAmount;
	}

	public String getTransactionCurrency() {
		return transactionCurrency;
	}

	@Override
	public int hashCode() {
		return Objects.hash(accountId, transactionAmount, transactionCurrency);
	}


	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public void setTransactionAmount(BigDecimal transactionAmount) {
		this.transactionAmount = transactionAmount;
	}

	public void setTransactionCurrency(String transactionCurrency) {
		this.transactionCurrency = transactionCurrency;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TransactionInput other = (TransactionInput) obj;
		return Objects.equals(accountId, other.accountId) && Objects.equals(transactionAmount, other.transactionAmount)
				&& Objects.equals(transactionCurrency, other.transactionCurrency);
	}

	
	
}
