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

import java.math.BigDecimal;
import java.time.LocalDate;
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
    public void testGetTransactionsByUserId() throws Exception {
        int userId = 1;
        List<Transaction> transactions = Arrays.asList(
                new Transaction(userId, 1, "Vendor1", BigDecimal.valueOf(100), null, "Description1", LocalDate.now()),
                new Transaction(userId, 2, "Vendor2", BigDecimal.valueOf(200), null, "Description2", LocalDate.now())
        );

        when(transactionService.getTransactionsByUserId(userId)).thenReturn(transactions);

        mockMvc.perform(get("/transactionsPrivate/user/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].userId").value(userId))
                .andExpect(jsonPath("$[1].userId").value(userId));

        verify(transactionService).getTransactionsByUserId(userId);
    }

    @Test
    public void testGetTransactionsByUserIdExcludeIncome() throws Exception {
        int userId = 1;
        List<Transaction> transactions = Arrays.asList(
                new Transaction(userId, 1, "Vendor1", BigDecimal.valueOf(100), null, "Description1", LocalDate.now()),
                new Transaction(userId, 2, "Vendor2", BigDecimal.valueOf(200), null, "Description2", LocalDate.now())
        );

        when(transactionService.getTransactionsByUserIdExcludingIncome(userId)).thenReturn(transactions);

        mockMvc.perform(get("/transactionsPrivate/budget/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].userId").value(userId))
                .andExpect(jsonPath("$[1].userId").value(userId));

        verify(transactionService).getTransactionsByUserIdExcludingIncome(userId);
    }

    @Test
    public void testGetTransactionsByAccountId() throws Exception {
        int accountId = 1;
        List<Transaction> transactions = Arrays.asList(
                new Transaction(1, accountId, "Vendor1", BigDecimal.valueOf(100), null, "Description1", LocalDate.now()),
                new Transaction(2, accountId, "Vendor2", BigDecimal.valueOf(200), null, "Description2", LocalDate.now())
        );

        when(transactionService.getTransactionsByAccountId(accountId)).thenReturn(transactions);

        mockMvc.perform(get("/transactionsPrivate/account/{accountId}", accountId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].accountId").value(accountId))
                .andExpect(jsonPath("$[1].accountId").value(accountId));

        verify(transactionService).getTransactionsByAccountId(accountId);
    }

    @Test
    public void testDeleteTransactionByUserId() throws Exception {
        int userId = 1;

        doNothing().when(transactionService).deleteTransactionByUserId(userId);

        mockMvc.perform(delete("/transactionsPrivate/deleteTransaction/user/{userId}", userId))
                .andExpect(status().isNoContent());

        verify(transactionService).deleteTransactionByUserId(userId);
    }
}