package com.jpminterview.util;

public enum Message {
	
	OK("OK"),
	ACCOUNT_NOT_FOUND("Could not find the account no, Please check again."),
	TRANSACTION_UNSUCCESSFUL("Transaction unsuccessfull !!!"),
	INSUFFICIENT_FUNDS("Insufficient Funds !!!"),
	NO_TRANSACTIONS_FOUND("No Transactions found !!!");
	
	private String messageDesc;

	private Message(String messageDesc) {
		this.messageDesc = messageDesc;
	}

	public String getMessageDesc() {
		return messageDesc;
	}
	
}
