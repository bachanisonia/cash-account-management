package com.jpminterview.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.LinkedMultiValueMap;

import com.jpminterview.dto.AccountInput;
import com.jpminterview.dto.AccountResponse;
import com.jpminterview.dto.TransactionInput;
import com.jpminterview.dto.TransactionResponse;
import com.jpminterview.entity.Account;
import com.jpminterview.service.CashAccountService;
import com.jpminterview.util.JsonUtil;
import com.jpminterview.util.Message;

import net.bytebuddy.NamingStrategy.Suffixing.BaseNameResolver.ForGivenType;

@ExtendWith(SpringExtension.class)
@WebMvcTest(AccountController.class)
class AccountControllerTest {
	
	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private CashAccountService accountService;
	

	@Nested
	@DisplayName("getAccountInfo")
	class AccountInfoTest {
		
		@Test
		@DisplayName("Invalid Account Number")
		void testInvalidAccountNumber() throws Exception {
			String accountId = "ACCOUNT5670";
			when(accountService.getAccountDetails(accountId)).thenReturn(new AccountResponse(Message.ACCOUNT_NOT_FOUND));
			
			mvc.perform(MockMvcRequestBuilders.post("/cash-management/accounts")
					.content(JsonUtil.toJson(accountId))
					.contentType(MediaType.APPLICATION_JSON))
					.andExpect(MockMvcResultMatchers.status().isBadRequest());
		}	
		
		@Test
		@DisplayName("valid Account Number")
		void testValidAccountNumber() throws Exception {
			
			String accountId = "ACCOUNT5285";
			Account account = new Account("ACCOUNT5285", "GBP", new BigDecimal(500.0), new BigDecimal(500.0), new BigDecimal(500.0));
			when(accountService.getAccountDetails(accountId)).thenReturn(new AccountResponse(account, Message.OK));
			
			mvc.perform(MockMvcRequestBuilders.post("/cash-management/accounts")
					.content(JsonUtil.toJson(accountId))
					.contentType(MediaType.APPLICATION_JSON))
					.andExpect(MockMvcResultMatchers.status().isOk());
		}
		
	}
	
	
	@Nested
	@DisplayName("accountDebit")
	class DebitTest {
		
		@Test
		@DisplayName("Invalid Account Number")
		void testInvalidAccountNumber() throws Exception {
			
			TransactionInput transactionInput = new TransactionInput("ACCOUNT52850", new BigDecimal(100.0), "GBP");
			when(accountService.debit(transactionInput)).thenReturn(new TransactionResponse(Message.ACCOUNT_NOT_FOUND));
			
			mvc.perform(MockMvcRequestBuilders.post("/cash-management/debit")
					.content(JsonUtil.toJson(transactionInput))
					.contentType(MediaType.APPLICATION_JSON))
					.andExpect(MockMvcResultMatchers.status().isBadRequest());
		}
		
		@Test
		@DisplayName("Insufficient Funds")
		void testInsufficientFunds() throws Exception {
			
			String accountId = "ACCOUNT5285";
			TransactionInput transactionInput = new TransactionInput(accountId, new BigDecimal(1000.0), "GBP");
			TransactionResponse transactionResponse = new TransactionResponse(Message.INSUFFICIENT_FUNDS);

			when(accountService.debit(transactionInput)).thenReturn(transactionResponse);
			
			mvc.perform(MockMvcRequestBuilders.post("/cash-management/debit")
					.content(JsonUtil.toJson(transactionInput))
					.contentType(MediaType.APPLICATION_JSON))
					.andExpect(MockMvcResultMatchers.status().isBadRequest())
					.andExpect(MockMvcResultMatchers.content().string(Message.INSUFFICIENT_FUNDS.getMessageDesc()));
			
			
			
		}
		
		@Test
		@DisplayName("Successful Debit")
		void testSuccessfulDebit() throws Exception {
			
			String accountId = "ACCOUNT5285";
			TransactionInput transactionInput = new TransactionInput(accountId, new BigDecimal(1000.0), "GBP");
			TransactionResponse transactionResponse = new TransactionResponse(Message.OK);

			when(accountService.debit(transactionInput)).thenReturn(transactionResponse);
			
			mvc.perform(MockMvcRequestBuilders.post("/cash-management/debit")
					.content(JsonUtil.toJson(transactionInput))
					.contentType(MediaType.APPLICATION_JSON))
					.andExpect(MockMvcResultMatchers.status().isOk());
		}
	}

	
	@Nested
	@DisplayName("accountCredit")
	class CreditTest {
		
		@Test
		@DisplayName("Invalid Account Number")
		void testInvalidAccountNumber() throws Exception {
			
			TransactionInput transactionInput = new TransactionInput("ACCOUNT52850", new BigDecimal(100.0), "GBP");
			when(accountService.credit(transactionInput)).thenReturn(new TransactionResponse(Message.ACCOUNT_NOT_FOUND));
			
			mvc.perform(MockMvcRequestBuilders.post("/cash-management/credit")
					.content(JsonUtil.toJson(transactionInput))
					.contentType(MediaType.APPLICATION_JSON))
					.andExpect(MockMvcResultMatchers.status().isBadRequest());
		}
		
		@Test
		@DisplayName("Successful Credit")
		void testSuccessfulCredit() throws Exception {
			
			String accountId = "ACCOUNT5285";
			TransactionInput transactionInput = new TransactionInput(accountId, new BigDecimal(1000.0), "GBP");
			TransactionResponse transactionResponse = new TransactionResponse(Message.OK);

			when(accountService.credit(transactionInput)).thenReturn(transactionResponse);
			
			mvc.perform(MockMvcRequestBuilders.post("/cash-management/credit")
					.content(JsonUtil.toJson(transactionInput))
					.contentType(MediaType.APPLICATION_JSON))
					.andExpect(MockMvcResultMatchers.status().isOk());
			
		}
	}
}
