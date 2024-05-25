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

        mockMvc.perform(get("/transactions").header("USER-ID", "1"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.length()").value(transactions.size()));

        verify(transactionService, times(1)).getTransactionsByUserId(userId);
    }

    @Test
    public void testGetRecentFiveTransactions() throws Exception {
        int userId = 1;
        List<Transaction> transactions = Arrays.asList(new Transaction(), new Transaction());
        when(transactionService.getRecentFiveTransactions(userId)).thenReturn(transactions);

        mockMvc.perform(get("/transactions/recentTransactions").header("USER-ID", "1"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.length()").value(transactions.size()));

        verify(transactionService, times(1)).getRecentFiveTransactions(userId);
    }

    @Test
    public void testGetTransactionsFromCurrentMonth() throws Exception {
        int userId = 1;
        List<Transaction> transactions = Arrays.asList(new Transaction(), new Transaction());
        when(transactionService.getTransactionsFromCurrentMonth(userId)).thenReturn(transactions);

        mockMvc.perform(get("/transactions/currentMonthTransactions").header("USER-ID", "1"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.length()").value(transactions.size()));

        verify(transactionService, times(1)).getTransactionsFromCurrentMonth(userId);
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
        when(transactionService.createTransaction(eq(1), any(Transaction.class))).thenReturn(transaction);

        mockMvc.perform(post("/transactions")
                .header("USER-ID", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"accountId\":1,\"userId\":1,\"vendorName\":\"Vendor\",\"amount\":100,\"category\":\"SHOPPING\",\"date\":\"" + LocalDate.now() + "\"}"))
               .andExpect(status().isCreated())
               .andExpect(jsonPath("$.accountId").value(1))
               .andExpect(jsonPath("$.userId").value(1))
               .andExpect(jsonPath("$.vendorName").value("Vendor"));

        verify(transactionService, times(1)).createTransaction(eq(1), any(Transaction.class));
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
        when(transactionService.updateTransaction(eq(1), eq(1), any(Transaction.class))).thenReturn(transaction);

        mockMvc.perform(put("/transactions/1")
                .header("USER-ID", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"transactionId\":1,\"accountId\":1,\"userId\":1,\"vendorName\":\"Vendor\",\"amount\":100,\"category\":\"SHOPPING\",\"date\":\"" + LocalDate.now() + "\"}"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.transactionId").value(1))
               .andExpect(jsonPath("$.accountId").value(1))
               .andExpect(jsonPath("$.userId").value(1))
               .andExpect(jsonPath("$.vendorName").value("Vendor"));

        verify(transactionService, times(1)).updateTransaction(eq(1), eq(1), any(Transaction.class));
    }

    @Test
    public void testDeleteTransaction() throws Exception {
        int transactionId = 1;
        doNothing().when(transactionService).deleteTransaction(transactionId);

        mockMvc.perform(delete("/transactions/{transactionId}", transactionId))
               .andExpect(status().isNoContent());

        verify(transactionService, times(1)).deleteTransaction(transactionId);
    }
}
