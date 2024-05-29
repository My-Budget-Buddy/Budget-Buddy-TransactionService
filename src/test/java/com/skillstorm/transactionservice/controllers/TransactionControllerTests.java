package com.skillstorm.transactionservice.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skillstorm.transactionservice.models.Transaction;
import com.skillstorm.transactionservice.services.TransactionService;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
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

    @Autowired
    private ObjectMapper objectMapper;

    @Captor
    private ArgumentCaptor<HttpHeaders> headersCaptor;

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
        List<Transaction> transactions = Arrays.asList(
                new Transaction(userId, 1, "Vendor1", BigDecimal.valueOf(100), null, "Description1", LocalDate.now()),
                new Transaction(userId, 2, "Vendor2", BigDecimal.valueOf(200), null, "Description2", LocalDate.now())
        );

        HttpHeaders headers = new HttpHeaders();
        headers.add("User-ID", String.valueOf(userId));

        when(transactionService.getTransactionsByUserId(userId)).thenReturn(transactions);

        mockMvc.perform(get("/transactions")
                        .headers(headers))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].userId").value(userId))
                .andExpect(jsonPath("$[1].userId").value(userId));

        verify(transactionService).validateRequestWithHeaders(headersCaptor.capture());
        verify(transactionService).getTransactionsByUserId(userId);
    }

    @Test
    public void testGetRecentFiveTransactions() throws Exception {
        int userId = 1;
        List<Transaction> transactions = Arrays.asList(
                new Transaction(userId, 1, "Vendor1", BigDecimal.valueOf(100), null, "Description1", LocalDate.now()),
                new Transaction(userId, 2, "Vendor2", BigDecimal.valueOf(200), null, "Description2", LocalDate.now())
        );

        HttpHeaders headers = new HttpHeaders();
        headers.add("User-ID", String.valueOf(userId));

        when(transactionService.getRecentFiveTransactions(userId)).thenReturn(transactions);

        mockMvc.perform(get("/transactions/recentTransactions")
                        .headers(headers))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].userId").value(userId))
                .andExpect(jsonPath("$[1].userId").value(userId));

        verify(transactionService).validateRequestWithHeaders(headersCaptor.capture());
        verify(transactionService).getRecentFiveTransactions(userId);
    }

    @Test
    public void testGetTransactionsFromCurrentMonth() throws Exception {
        int userId = 1;
        List<Transaction> transactions = Arrays.asList(
                new Transaction(userId, 1, "Vendor1", BigDecimal.valueOf(100), null, "Description1", LocalDate.now()),
                new Transaction(userId, 2, "Vendor2", BigDecimal.valueOf(200), null, "Description2", LocalDate.now())
        );

        HttpHeaders headers = new HttpHeaders();
        headers.add("User-ID", String.valueOf(userId));

        when(transactionService.getTransactionsFromCurrentMonth(userId)).thenReturn(transactions);

        mockMvc.perform(get("/transactions/currentMonthTransactions")
                        .headers(headers))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].userId").value(userId))
                .andExpect(jsonPath("$[1].userId").value(userId));

        verify(transactionService).validateRequestWithHeaders(headersCaptor.capture());
        verify(transactionService).getTransactionsFromCurrentMonth(userId);
    }

    @Test
    public void testGetTransactionsByUserIdAndVendorName() throws Exception {
        int userId = 1;
        String vendorName = "Vendor";
        List<Transaction> transactions = Arrays.asList(
                new Transaction(userId, 1, vendorName, BigDecimal.valueOf(100), null, "Description1", LocalDate.now()),
                new Transaction(userId, 2, vendorName, BigDecimal.valueOf(200), null, "Description2", LocalDate.now())
        );

        HttpHeaders headers = new HttpHeaders();
        headers.add("User-ID", String.valueOf(userId));

        when(transactionService.getTransactionsByUserIdAndVendorName(userId, vendorName)).thenReturn(transactions);

        mockMvc.perform(get("/transactions/vendor/{vendorName}", vendorName)
                        .headers(headers))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].userId").value(userId))
                .andExpect(jsonPath("$[1].userId").value(userId));

        verify(transactionService).validateRequestWithHeaders(headersCaptor.capture());
        verify(transactionService).getTransactionsByUserIdAndVendorName(userId, vendorName);
    }

    @Test
    public void testCreateTransaction() throws Exception {
        int userId = 1;
        Transaction transaction = new Transaction(userId, 1, "Vendor", BigDecimal.valueOf(100), null, "Description", LocalDate.now());
        transaction.setTransactionId(1);

        HttpHeaders headers = new HttpHeaders();
        headers.add("User-ID", String.valueOf(userId));

        when(transactionService.createTransaction(eq(userId), any(Transaction.class))).thenReturn(transaction);

        mockMvc.perform(post("/transactions")
                        .headers(headers)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transaction)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.transactionId").value(transaction.getTransactionId()))
                .andExpect(jsonPath("$.userId").value(transaction.getUserId()))
                .andExpect(jsonPath("$.vendorName").value(transaction.getVendorName()))
                .andExpect(jsonPath("$.amount").value(transaction.getAmount().doubleValue()))
                .andExpect(jsonPath("$.description").value(transaction.getDescription()));

        verify(transactionService).validateRequestWithHeaders(headersCaptor.capture());
        verify(transactionService).createTransaction(eq(userId), any(Transaction.class));
    }

    @Test
    public void testUpdateTransaction() throws Exception {
        int userId = 1;
        int transactionId = 1;
        Transaction transaction = new Transaction(userId, 1, "Vendor", BigDecimal.valueOf(100), null, "Description", LocalDate.now());

        Transaction updatedTransaction = new Transaction(userId, 1, "Updated Vendor", BigDecimal.valueOf(200), null, "Updated Description", LocalDate.now());
        updatedTransaction.setTransactionId(transactionId);

        HttpHeaders headers = new HttpHeaders();
        headers.add("User-ID", String.valueOf(userId));

        when(transactionService.updateTransaction(eq(transactionId), eq(userId), any(Transaction.class))).thenReturn(updatedTransaction);

        mockMvc.perform(put("/transactions/{transactionId}", transactionId)
                        .headers(headers)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transaction)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transactionId").value(updatedTransaction.getTransactionId()))
                .andExpect(jsonPath("$.userId").value(updatedTransaction.getUserId()))
                .andExpect(jsonPath("$.vendorName").value(updatedTransaction.getVendorName()))
                .andExpect(jsonPath("$.amount").value(updatedTransaction.getAmount().doubleValue()))
                .andExpect(jsonPath("$.description").value(updatedTransaction.getDescription()));

        verify(transactionService).validateRequestWithHeaders(headersCaptor.capture());
        verify(transactionService).updateTransaction(eq(transactionId), eq(userId), any(Transaction.class));
    }

    @Test
    public void testDeleteTransaction() throws Exception {
        int transactionId = 1;
        HttpHeaders headers = new HttpHeaders();
        headers.add("User-ID", "1");

        doNothing().when(transactionService).deleteTransaction(transactionId);

        mockMvc.perform(delete("/transactions/{transactionId}", transactionId)
                        .headers(headers))
                .andExpect(status().isNoContent());

        verify(transactionService).validateRequestWithHeaders(headersCaptor.capture());
        verify(transactionService).deleteTransaction(transactionId);
    }
}
