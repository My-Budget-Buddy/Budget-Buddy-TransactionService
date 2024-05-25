package com.skillstorm.transactionservice.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;

import com.skillstorm.transactionservice.models.Transaction;
import com.skillstorm.transactionservice.services.TransactionService;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

@WebMvcTest(TransactionController.class)
public class TransactionBetweenServicesControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionService transactionService;

    private AutoCloseable closeable;

    @BeforeEach
    public void setup() {
        closeable = MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(new TransactionBetweenServicesController(transactionService)).build();
    }

    @AfterEach
    public void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    public void testGetTransactionsByUserIdExcludeIncome() throws Exception {
        int userId = 1;
        List<Transaction> transactions = Arrays.asList(new Transaction(), new Transaction());
        when(transactionService.getTransactionsByUserIdExcludingIncome(userId)).thenReturn(transactions);

        mockMvc.perform(get("/transactionsPrivate/budget/{userId}", userId))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.length()").value(transactions.size()));

        verify(transactionService, times(1)).getTransactionsByUserIdExcludingIncome(userId);
    }

    @Test
    public void testGetTransactionsByAccountId() throws Exception {
        int accountId = 1;
        List<Transaction> transactions = Arrays.asList(new Transaction(), new Transaction());
        when(transactionService.getTransactionsByAccountId(accountId)).thenReturn(transactions);

        mockMvc.perform(get("/transactionsPrivate/account/{accountId}", accountId))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.length()").value(transactions.size()));

        verify(transactionService, times(1)).getTransactionsByAccountId(accountId);
    }
}