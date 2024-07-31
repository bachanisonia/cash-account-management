package com.jpminterview.dto;

import java.util.Objects;

import com.jpminterview.entity.Account;
import com.jpminterview.util.Message;

public class AccountResponse {

	private Account account;
	private Message message;
	
	public AccountResponse() {
	}

	public AccountResponse(Message message) {
		this.message = message;
	}

	public AccountResponse(Account account, Message message) {
		this.account = account;
		this.message = message;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}

	@Override
	public int hashCode() {
		return Objects.hash(account, message);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AccountResponse other = (AccountResponse) obj;
		return Objects.equals(account, other.account) && message == other.message;
	}
	
}
