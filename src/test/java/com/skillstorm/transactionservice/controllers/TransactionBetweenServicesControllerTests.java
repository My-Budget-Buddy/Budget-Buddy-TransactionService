package com.skillstorm.transactionservice.controllers;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.skillstorm.transactionservice.models.Transaction;
import com.skillstorm.transactionservice.services.TransactionService;

@ExtendWith(MockitoExtension.class)
public class TransactionBetweenServicesControllerTests {



    @Mock
    private TransactionService transactionService; // Mock

    @InjectMocks    
    private TransactionBetweenServicesController transactionBetweenServicesController; // System under test
    
    @Test
    void testDeleteTransactionByUserId() {
        // Arrange
        doNothing().when(transactionService).deleteTransactionByUserId(anyInt());
        // Act
        ResponseEntity<Void> actual = transactionBetweenServicesController.deleteTransactionByUserId(1);
        // Assert
        assertEquals(HttpStatus.NO_CONTENT, actual.getStatusCode());

    }

    @Test
    void testGetTransactionsByAccountId() {
        // Arrange
        List<Transaction> transactionsList = new ArrayList<>();
        when(transactionService.getTransactionsByAccountId(anyInt())).thenReturn(transactionsList);
        // Act
        ResponseEntity<List<Transaction>> actual = transactionBetweenServicesController.getTransactionsByAccountId(1);
        // Assert
        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertEquals(transactionsList, actual.getBody());

    }

    @Test
    void testGetTransactionsByUserId() {
        // Arrange
        List<Transaction> transactionsList = new ArrayList<>();
        when(transactionService.getTransactionsByUserId(anyInt())).thenReturn(transactionsList);
        // Act
        ResponseEntity<List<Transaction>> actual = transactionBetweenServicesController.getTransactionsByUserId(1);
        // Assert
        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertEquals(transactionsList, actual.getBody());

    }

    @Test
    void testGetTransactionsByUserIdExcludeIncome() {
        // Arrange
        List<Transaction> transactionsList = new ArrayList<>();
        when(transactionService.getTransactionsByUserIdExcludingIncome(anyInt())).thenReturn(transactionsList);
        // Act
        ResponseEntity<List<Transaction>> actual = transactionBetweenServicesController.getTransactionsByUserIdExcludeIncome(1);
        // Assert
        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertEquals(transactionsList, actual.getBody());
    }
}
