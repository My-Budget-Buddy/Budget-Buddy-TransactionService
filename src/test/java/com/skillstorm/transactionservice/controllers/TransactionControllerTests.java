package com.skillstorm.transactionservice.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;

import com.skillstorm.transactionservice.models.Transaction;
import com.skillstorm.transactionservice.models.TransactionCategory;
import com.skillstorm.transactionservice.services.TransactionService;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@WebMvcTest(TransactionController.class)
public class TransactionControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionService transactionService;

    private AutoCloseable closeable;

    @BeforeEach
    public void setup() {
        closeable = MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(new TransactionController(transactionService)).build();
    }

    @AfterEach
    public void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    public void testGetTransactionsByUserId() throws Exception {
        int userId = 1;
        List<Transaction> transactions = Arrays.asList(new Transaction(), new Transaction());
        when(transactionService.getTransactionsByUserId(userId)).thenReturn(transactions);

        mockMvc.perform(get("/transactions/user/{userId}", userId))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.length()").value(transactions.size()));

        verify(transactionService, times(1)).getTransactionsByUserId(userId);
    }

    @Test
    public void testGetRecentFiveTransactions() throws Exception {
        int userId = 1;
        List<Transaction> transactions = Arrays.asList(new Transaction(), new Transaction());
        when(transactionService.getRecentFiveTransactions(userId)).thenReturn(transactions);

        mockMvc.perform(get("/transactions/recentTransactions/{userId}", userId))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.length()").value(transactions.size()));

        verify(transactionService, times(1)).getRecentFiveTransactions(userId);
    }

    @Test
    public void testGetTransactionsFromCurrentMonth() throws Exception {
        int userId = 1;
        List<Transaction> transactions = Arrays.asList(new Transaction(), new Transaction());
        when(transactionService.getTransactionsFromCurrentMonth(userId)).thenReturn(transactions);

        mockMvc.perform(get("/transactions/currentMonthTransactions/{userId}", userId))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.length()").value(transactions.size()));

        verify(transactionService, times(1)).getTransactionsFromCurrentMonth(userId);
    }

    @Test
    public void testGetTransactionsByUserIdExcludeIncome() throws Exception {
        int userId = 1;
        List<Transaction> transactions = Arrays.asList(new Transaction(), new Transaction());
        when(transactionService.getTransactionsByUserIdExcludingIncome(userId)).thenReturn(transactions);

        mockMvc.perform(get("/transactions/budget/{userId}", userId))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.length()").value(transactions.size()));

        verify(transactionService, times(1)).getTransactionsByUserIdExcludingIncome(userId);
    }

    @Test
    public void testGetTransactionsByAccountId() throws Exception {
        int accountId = 1;
        List<Transaction> transactions = Arrays.asList(new Transaction(), new Transaction());
        when(transactionService.getTransactionsByAccountId(accountId)).thenReturn(transactions);

        mockMvc.perform(get("/transactions/account/{accountId}", accountId))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.length()").value(transactions.size()));

        verify(transactionService, times(1)).getTransactionsByAccountId(accountId);
    }

    @Test
    public void testCreateTransaction() throws Exception {
        Transaction transaction = new Transaction();
        transaction.setAccountId(1);
        transaction.setUserId(1);
        transaction.setVendorName("Vendor");
        transaction.setAmount(new BigDecimal("100"));
        transaction.setCategory(TransactionCategory.SHOPPING);
        transaction.setDate(LocalDate.now());
        when(transactionService.createTransaction(any(Transaction.class))).thenReturn(transaction);

        mockMvc.perform(post("/transactions/createTransaction")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"accountId\":1,\"userId\":1,\"vendorName\":\"Vendor\",\"amount\":100,\"category\":\"SHOPPING\",\"date\":\"" + LocalDate.now() + "\"}"))
               .andExpect(status().isCreated())
               .andExpect(jsonPath("$.accountId").value(1))
               .andExpect(jsonPath("$.userId").value(1))
               .andExpect(jsonPath("$.vendorName").value("Vendor"));

        verify(transactionService, times(1)).createTransaction(any(Transaction.class));
    }

    @Test
    public void testUpdateTransaction() throws Exception {
        Transaction transaction = new Transaction();
        transaction.setTransactionId(1);
        transaction.setAccountId(1);
        transaction.setUserId(1);
        transaction.setVendorName("Vendor");
        transaction.setAmount(new BigDecimal("100"));
        transaction.setCategory(TransactionCategory.SHOPPING);
        transaction.setDate(LocalDate.now());
        when(transactionService.updateTransaction(any(Transaction.class))).thenReturn(transaction);

        mockMvc.perform(put("/transactions/updateTransaction")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"transactionId\":1,\"accountId\":1,\"userId\":1,\"vendorName\":\"Vendor\",\"amount\":100,\"category\":\"SHOPPING\",\"date\":\"" + LocalDate.now() + "\"}"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.transactionId").value(1))
               .andExpect(jsonPath("$.accountId").value(1))
               .andExpect(jsonPath("$.userId").value(1))
               .andExpect(jsonPath("$.vendorName").value("Vendor"));

        verify(transactionService, times(1)).updateTransaction(any(Transaction.class));
    }

    @Test
    public void testDeleteTransaction() throws Exception {
        int transactionId = 1;
        doNothing().when(transactionService).deleteTransaction(transactionId);

        mockMvc.perform(delete("/transactions/deleteTransaction/{transactionId}", transactionId))
               .andExpect(status().isNoContent());

        verify(transactionService, times(1)).deleteTransaction(transactionId);
    }
}
