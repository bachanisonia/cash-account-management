package com.jpminterview.entity;


public enum TransactionType {

	CREDIT("Credit"),
	DEBIT("Debit");
	
	private String transactionType;

	private TransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public String getTransactionType() {
		return transactionType;
	}
	
}
