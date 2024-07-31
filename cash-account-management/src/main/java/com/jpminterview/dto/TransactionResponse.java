package com.jpminterview.dto;

import java.util.Objects;

import com.jpminterview.entity.Transaction;
import com.jpminterview.util.Message;

public class TransactionResponse {

	private Transaction transaction;
	private Message message;
	
	public TransactionResponse() {
	}
	
	public TransactionResponse(Message message) {
		this.message = message;
	}
	
	public TransactionResponse(Transaction transaction, Message message) {
		this.transaction = transaction;
		this.message = message;
	}

	public void setTransaction(Transaction transaction) {
		this.transaction = transaction;
	}

	public void setMessage(Message message) {
		this.message = message;
	}

	public Transaction getTransaction() {
		return transaction;
	}

	public Message getMessage() {
		return message;
	}

	@Override
	public int hashCode() {
		return Objects.hash(message, transaction);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TransactionResponse other = (TransactionResponse) obj;
		return message == other.message && Objects.equals(transaction, other.transaction);
	}
	
}
