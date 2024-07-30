package com.jpminterview.dto;

import java.util.Objects;

public class AccountInput {

	String accountId;

	public AccountInput() {
	}

	public AccountInput(String accountId) {
		this.accountId = accountId;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
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
		AccountInput other = (AccountInput) obj;
		return Objects.equals(accountId, other.accountId);
	}

	
	
}
