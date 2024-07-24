package com.skillstorm.transactionservice.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.skillstorm.transactionservice.exceptions.InvalidTransactionException;
import com.skillstorm.transactionservice.exceptions.TransactionNotFoundException;
import com.skillstorm.transactionservice.models.Transaction;
import com.skillstorm.transactionservice.models.TransactionCategory;
import com.skillstorm.transactionservice.repositories.TransactionRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@SpringBootTest
@Transactional
public class TransactionServiceIntegrationTests {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private TransactionRepository transactionRepository;

    @BeforeEach
    public void setup() {
        transactionRepository.deleteAll();
    }

    @AfterEach
    public void tearDown() {
        transactionRepository.deleteAll();
    }

    @Test
    public void testGetTransactionsByUserId_Success() {
        int userId = 1;
        Transaction transaction1 = new Transaction(userId, 1, "Vendor1", BigDecimal.valueOf(100), TransactionCategory.SHOPPING, "Description1", LocalDate.now());
        Transaction transaction2 = new Transaction(userId, 2, "Vendor2", BigDecimal.valueOf(200), TransactionCategory.SHOPPING, "Description2", LocalDate.now());
        transactionRepository.saveAll(List.of(transaction1, transaction2));

        List<Transaction> result = transactionService.getTransactionsByUserId(userId);

        assertEquals(2, result.size());
        assertTrue(result.contains(transaction1));
        assertTrue(result.contains(transaction2));
    }

    @Test
    public void testGetTransactionsByUserId_NotFound() {
        int userId = 1;

        assertThrows(TransactionNotFoundException.class, () -> transactionService.getTransactionsByUserId(userId));
    }

    @Test
    public void testGetTransactionsByAccountId_Success() {
        int accountId = 1;
        Transaction transaction1 = new Transaction(1, accountId, "Vendor1", BigDecimal.valueOf(100), TransactionCategory.SHOPPING, "Description1", LocalDate.now());
        Transaction transaction2 = new Transaction(2, accountId, "Vendor2", BigDecimal.valueOf(200), TransactionCategory.SHOPPING, "Description2", LocalDate.now());
        transactionRepository.saveAll(List.of(transaction1, transaction2));

        List<Transaction> result = transactionService.getTransactionsByAccountId(accountId);

        assertEquals(2, result.size());
        assertTrue(result.contains(transaction1));
        assertTrue(result.contains(transaction2));
    }

    @Test
    public void testGetTransactionsByAccountId_NotFound() {
        int accountId = 1;

        assertThrows(TransactionNotFoundException.class, () -> transactionService.getTransactionsByAccountId(accountId));
    }

    @Test
    public void testGetTransactionsByUserIdAndVendorName_Success() {
        int userId = 1;
        String vendorName = "Vendor1";
        Transaction transaction1 = new Transaction(userId, 1, vendorName, BigDecimal.valueOf(100), TransactionCategory.SHOPPING, "Description1", LocalDate.now());
        transactionRepository.save(transaction1);

        List<Transaction> result = transactionService.getTransactionsByUserIdAndVendorName(userId, vendorName);

        assertEquals(1, result.size());
        assertTrue(result.contains(transaction1));
    }

    @Test
    public void testGetTransactionsByUserIdAndVendorName_NotFound() {
        int userId = 1;
        String vendorName = "Vendor1";

        assertThrows(TransactionNotFoundException.class, () -> transactionService.getTransactionsByUserIdAndVendorName(userId, vendorName));
    }

    @Test
    public void testGetRecentFiveTransactions_Success() {
        int userId = 1;
        Transaction transaction1 = new Transaction(userId, 1, "Vendor1", BigDecimal.valueOf(100), TransactionCategory.SHOPPING, "Description1", LocalDate.now());
        Transaction transaction2 = new Transaction(userId, 2, "Vendor2", BigDecimal.valueOf(200), TransactionCategory.SHOPPING, "Description2", LocalDate.now());
        Transaction transaction3 = new Transaction(userId, 3, "Vendor3", BigDecimal.valueOf(300), TransactionCategory.SHOPPING, "Description3", LocalDate.now());
        Transaction transaction4 = new Transaction(userId, 4, "Vendor4", BigDecimal.valueOf(400), TransactionCategory.SHOPPING, "Description4", LocalDate.now());
        Transaction transaction5 = new Transaction(userId, 5, "Vendor5", BigDecimal.valueOf(500), TransactionCategory.SHOPPING, "Description5", LocalDate.now());
        Transaction transaction6 = new Transaction(userId, 6, "Vendor6", BigDecimal.valueOf(600), TransactionCategory.SHOPPING, "Description6", LocalDate.now());
        transactionRepository.saveAll(List.of(transaction1, transaction2, transaction3, transaction4, transaction5, transaction6));

        List<Transaction> result = transactionService.getRecentFiveTransactions(userId);

        assertEquals(5, result.size());
        assertTrue(result.contains(transaction1));
        assertTrue(result.contains(transaction2));
        assertTrue(result.contains(transaction3));
        assertTrue(result.contains(transaction4));
        assertTrue(result.contains(transaction5));
    }

    @Test
    public void testGetRecentFiveTransactions_NotFound() {
        int userId = 1;

        assertThrows(TransactionNotFoundException.class, () -> transactionService.getRecentFiveTransactions(userId));
    }

    @Test
    public void testGetTransactionsFromCurrentMonth_Success() {
        int userId = 1;
        LocalDate currentDate = LocalDate.now();
        Transaction transaction1 = new Transaction(userId, 1, "Vendor1", BigDecimal.valueOf(100), TransactionCategory.SHOPPING, "Description1", currentDate);
        Transaction transaction2 = new Transaction(userId, 2, "Vendor2", BigDecimal.valueOf(200), TransactionCategory.SHOPPING, "Description2", currentDate);
        transactionRepository.saveAll(List.of(transaction1, transaction2));

        List<Transaction> result = transactionService.getTransactionsFromCurrentMonth(userId);

        assertEquals(2, result.size());
        assertTrue(result.contains(transaction1));
        assertTrue(result.contains(transaction2));
    }

    @Test
    public void testCreateTransaction_Success() {
        int userId = 1;
        Transaction transaction = new Transaction(1, userId, "Vendor", BigDecimal.valueOf(100), TransactionCategory.SHOPPING, "Description", LocalDate.now());

        Transaction result = transactionService.createTransaction(userId, transaction);

        assertNotNull(result);
        assertNotNull(result.getTransactionId());
        assertEquals(userId, result.getUserId());
        assertEquals("Vendor", result.getVendorName());
    }

    @Test
    public void testCreateTransaction_InvalidAccountId() {
        int userId = 1;
        Transaction transaction = new Transaction(userId, 0, "Vendor", BigDecimal.valueOf(100), TransactionCategory.SHOPPING, "Description", LocalDate.now());

        assertThrows(InvalidTransactionException.class, () -> transactionService.createTransaction(userId, transaction));
    }

    @Test
    public void testUpdateTransaction_Success() {
        int userId = 1;
        Transaction transaction = new Transaction(1, userId, "Vendor", BigDecimal.valueOf(100), TransactionCategory.SHOPPING, "Description", LocalDate.now());
        transactionRepository.save(transaction);

        Transaction updateData = new Transaction();
        updateData.setVendorName("Updated Vendor");
        updateData.setAmount(BigDecimal.valueOf(200));

        Transaction result = transactionService.updateTransaction(transaction.getTransactionId(), userId, updateData);

        assertEquals("Updated Vendor", result.getVendorName());
        assertEquals(BigDecimal.valueOf(200), result.getAmount());
    }

    @Test
    public void testUpdateTransaction_NotFound() {
        int userId = 1;
        Transaction updateData = new Transaction();
        updateData.setVendorName("Updated Vendor");

        assertThrows(TransactionNotFoundException.class, () -> transactionService.updateTransaction(1, userId, updateData));
    }

    @Test
    public void testDeleteTransaction_Success() {
        Transaction transaction = new Transaction(1, 1, "Vendor", BigDecimal.valueOf(100), TransactionCategory.SHOPPING, "Description", LocalDate.now());
        transactionRepository.save(transaction);

        transactionService.deleteTransaction(transaction.getTransactionId());

        Optional<Transaction> result = transactionRepository.findById(transaction.getTransactionId());
        assertTrue(result.isEmpty());
    }

    @Test
    public void testDeleteTransaction_NotFound() {
        assertThrows(TransactionNotFoundException.class, () -> transactionService.deleteTransaction(1));
    }
}