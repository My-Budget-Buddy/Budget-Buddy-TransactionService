package com.skillstorm.transactionservice.controllers;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.mockito.InjectMocks;

import com.skillstorm.transactionservice.models.Transaction;
import com.skillstorm.transactionservice.services.TransactionService;

@ExtendWith(MockitoExtension.class)
public class TransactionControllerTests {

    @Mock
    private TransactionService transactionService; // Mock

    @InjectMocks
    private TransactionController transactionController; // System under test

    @Test
    void testCreateTransaction() {
        // Arrange
        Transaction transaction = new Transaction();
        HttpHeaders headers = new HttpHeaders();
        headers.add("User-ID", "1");
        doNothing().when(transactionService).validateRequestWithHeaders(any(HttpHeaders.class));
        when(transactionService.createTransaction(anyInt(), any(Transaction.class))).thenReturn(transaction);
        // Act
        ResponseEntity<Transaction> actual = transactionController.createTransaction(transaction, headers);
        // Assert
        assertEquals(HttpStatus.CREATED, actual.getStatusCode());
        assertEquals(transaction, actual.getBody());

    }

    @Test
    void testDeleteTransaction() {
        // Arrange
        HttpHeaders headers = new HttpHeaders();
        headers.add("User-ID", "1");
        doNothing().when(transactionService).validateRequestWithHeaders(any(HttpHeaders.class));
        doNothing().when(transactionService).deleteTransaction(anyInt());
        // Act
        ResponseEntity<Void> actual = transactionController.deleteTransaction(1, headers);
        // Assert
        assertEquals(HttpStatus.NO_CONTENT, actual.getStatusCode());
    }

    @Test
    void testGetRecentFiveTransactions() {
        // Arrange
        List<Transaction> transactions = new ArrayList<>();
        HttpHeaders headers = new HttpHeaders();
        headers.add("User-ID", "1");
        doNothing().when(transactionService).validateRequestWithHeaders(any(HttpHeaders.class));
        when(transactionService.getRecentFiveTransactions(1)).thenReturn(transactions);
        // Act
        ResponseEntity<List<Transaction>> actual = transactionController.getRecentFiveTransactions(headers);
        // Assert
        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertEquals(transactions, actual.getBody());
    }

    @Test
    void testGetTransactionsByUserId() {
        // Arrange
        List<Transaction> transactions = new ArrayList<>();
        HttpHeaders headers = new HttpHeaders();
        headers.add("User-ID", "1");
        doNothing().when(transactionService).validateRequestWithHeaders(any(HttpHeaders.class));
        when(transactionService.getTransactionsByUserId(1)).thenReturn(transactions);
        // Act
        ResponseEntity<List<Transaction>> actual = transactionController.getTransactionsByUserId(headers);
        // Assert
        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertEquals(transactions, actual.getBody());

    }

    @Test
    void testGetTransactionsByUserIdAndVendorName() {
        // Arrange
        List<Transaction> transactions = new ArrayList<>();
        HttpHeaders headers = new HttpHeaders();
        headers.add("User-ID", "1");
        doNothing().when(transactionService).validateRequestWithHeaders(any(HttpHeaders.class));
       
        when(transactionService.getTransactionsByUserIdAndVendorName(anyInt(), anyString())).thenReturn(transactions);
        // Act  
        ResponseEntity<List<Transaction>> actual = transactionController.getTransactionsByUserIdAndVendorName("vendorName", headers);
        // Assert
        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertEquals(transactions, actual.getBody());
    }

    @Test
    void testGetTransactionsFromCurrentMonth() {
        // Arrange
        List<Transaction> transactionsList = new ArrayList<>();
        HttpHeaders headers = new HttpHeaders();
        headers.add("User-ID", "1");
        doNothing().when(transactionService).validateRequestWithHeaders(any(HttpHeaders.class));
        when(transactionService.getTransactionsFromCurrentMonth(1)).thenReturn(transactionsList);
        // Act
        ResponseEntity<List<Transaction>> actual = transactionController.getTransactionsFromCurrentMonth(headers);
        // Assert
        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertEquals(transactionsList, actual.getBody());

    }

    @Test
    void testUpdateTransaction() {
        // Arrange
        Transaction transaction = new Transaction();
        HttpHeaders headers = new HttpHeaders();
        headers.add("User-ID", "1");
        doNothing().when(transactionService).validateRequestWithHeaders(any(HttpHeaders.class));
        when(transactionService.updateTransaction(anyInt(),anyInt(), any(Transaction.class))).thenReturn(transaction);
        // Act
        ResponseEntity<Transaction> actual = transactionController.updateTransaction(1, transaction, headers);
        // Assert
        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertEquals(transaction, actual.getBody());

    }
}
