package com.jpminterview.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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

import com.jpminterview.entity.Transaction;
import com.jpminterview.entity.TransactionType;
import com.jpminterview.service.CashTransactionService;
import com.jpminterview.util.JsonUtil;
import com.jpminterview.util.Message;

@ExtendWith(SpringExtension.class)
@WebMvcTest(TransactionController.class)
class TransactionControllerTest {

	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private CashTransactionService transactionService;
	
	@Nested
	@DisplayName("getAllTransactions")
	class AllTransactions {
		
		@Test
		@DisplayName("No transactions found")
		void testForInvalidAccountNumber() throws Exception {
			mvc.perform(MockMvcRequestBuilders.get("/cash-management/transactions"))
				.andExpect(MockMvcResultMatchers.status().isOk());
				//.andExpect(MockMvcResultMatchers.content().string(Message.NO_TRANSACTIONS_FOUND.getMessageDesc()));
			
		}
		
		@Test
		@DisplayName("Transactions found")
		void testForValidAccountNumber() throws Exception {
			List<Transaction> transactions = new ArrayList<>();
			transactions.add(new Transaction(1L, TransactionType.CREDIT, new BigDecimal(100.0), "GBP", "ACCOUNT5285"));
			transactions.add(new Transaction(2L, TransactionType.CREDIT, new BigDecimal(100.0), "GBP", "ACCOUNT5285"));
			when(transactionService.getAllTransactions()).thenReturn(transactions);
			
			mvc.perform(MockMvcRequestBuilders.get("/cash-management/transactions"))
			    .andExpect(MockMvcResultMatchers.status().isOk())
			    .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
			
		}
	}
	
	
	@Nested
	@DisplayName("getTransactionsForAnAccount")
	class AccountTransactions {
		
		@Test
		@DisplayName("Invalid Account Number")
		void testForInvalidAccountNumber() throws Exception {
			String accountId = "ACCOUNT52850";
			when(transactionService.getTransactionsForAnAccount(accountId)).thenReturn(new ArrayList<>());
					
			mvc.perform(MockMvcRequestBuilders.post("/cash-management/transactions")
				.contentType(MediaType.APPLICATION_JSON)
				.content(JsonUtil.toJson(accountId)))
				.andExpect(MockMvcResultMatchers.status().isOk());
				//.andExpect(MockMvcResultMatchers.content().string(Message.NO_TRANSACTIONS_FOUND.getMessageDesc()));
			
		}
		
		@Test
		@DisplayName("Valid Account Number")
		void testForValidAccountNumber() throws Exception {
			String accountId = "ACCOUNT5285";
			List<Transaction> transactions = new ArrayList<>();
			transactions.add(new Transaction(1L, TransactionType.CREDIT, new BigDecimal(100.0), "GBP", "ACCOUNT5285"));
			//transactions.add(new Transaction(2L, TransactionType.CREDIT, new BigDecimal(100.0), "GBP", "ACCOUNT5285"));
			when(transactionService.getTransactionsForAnAccount(accountId)).thenReturn(transactions);
			
			mvc.perform(MockMvcRequestBuilders.post("/cash-management/transactions")
				.contentType(MediaType.APPLICATION_JSON)
				.content(JsonUtil.toJson(accountId)))
			    .andExpect(MockMvcResultMatchers.status().isOk())
			    .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
			
		}
	}

}
