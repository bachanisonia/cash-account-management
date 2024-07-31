package com.jpminterview.entity;

import java.math.BigDecimal;
import java.util.Objects;

import org.springframework.stereotype.Component;

@Component
public class Transaction {

	// Transaction Reference
	Long transactionRef;
	
	// Credit/Debit
	String transactionType;
	
	BigDecimal transactionAmount;
	String transactionCurrency;
	String accountId;
	
	public Transaction() {
	}

	public Transaction(Long transactionRef, TransactionType transactionType, BigDecimal transactionAmount, String transactionCurrency,
			String accountId) {
		this.transactionRef = transactionRef;
		this.transactionType = TransactionType.CREDIT.getTransactionType();
		this.transactionAmount = transactionAmount;
		this.transactionCurrency = transactionCurrency;
		this.accountId = accountId;
	}

	public Long getTransactionRef() {
		return transactionRef;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public BigDecimal getTransactionAmount() {
		return transactionAmount;
	}

	public String getTransactionCurrency() {
		return transactionCurrency;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public void setTransactionAmount(BigDecimal transactionAmount) {
		this.transactionAmount = transactionAmount;
	}

	public void setTransactionCurrency(String transactionCurrency) {
		this.transactionCurrency = transactionCurrency;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	
	public void setTransactionRef(Long transactionRef) {
		this.transactionRef = transactionRef;
	}

	@Override
	public int hashCode() {
		return Objects.hash(accountId, transactionRef, transactionType);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Transaction other = (Transaction) obj;
		return Objects.equals(accountId, other.accountId) && Objects.equals(transactionRef, other.transactionRef)
				&& transactionType == other.transactionType;
	}
	
	
}
