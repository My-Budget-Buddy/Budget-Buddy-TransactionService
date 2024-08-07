package com.skillstorm.transactionservice.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.skillstorm.transactionservice.exceptions.InvalidTransactionException;
import com.skillstorm.transactionservice.exceptions.TransactionNotFoundException;
import com.skillstorm.transactionservice.models.Transaction;
import com.skillstorm.transactionservice.models.TransactionCategory;
import com.skillstorm.transactionservice.repositories.TransactionRepository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class TransactionServiceTests {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private TransactionService transactionService;

    private AutoCloseable closeable;

    @BeforeEach
    public void setup() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    public void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    public void testGetTransactionsByUserId_Success() {
        int userId = 1;
        List<Transaction> transactions = Arrays.asList(new Transaction(), new Transaction());
        when(transactionRepository.findByUserId(userId)).thenReturn(Optional.of(transactions));

        List<Transaction> result = transactionService.getTransactionsByUserId(userId);

        assertEquals(transactions, result);
        verify(transactionRepository, times(1)).findByUserId(userId);
    }

    @Test
    public void testGetTransactionsByUserId_NotFound() {
        int userId = 1;
        when(transactionRepository.findByUserId(userId)).thenReturn(Optional.empty());

        assertThrows(TransactionNotFoundException.class, () -> transactionService.getTransactionsByUserId(userId));
    }

    @Test
    public void testGetTransactionsByUserIdExcludingIncome_Success() {
        int userId = 1;
        String correlationId = UUID.randomUUID().toString();
        String replyToQueue = "budget-response";

        List<Transaction> transactions = Arrays.asList(new Transaction(), new Transaction());
        when(transactionRepository.findByUserIdExcludeIncome(userId)).thenReturn(Optional.of(transactions));

        // Method ultimately returns void, so without a returned object we verify the outgoing request:
        ArgumentCaptor<String> resultQueue = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<List<Transaction>> result = ArgumentCaptor.forClass(List.class);

        transactionService.getTransactionsByUserIdExcludingIncome(userId, correlationId, replyToQueue);
        verify(rabbitTemplate).convertAndSend(resultQueue.capture(), result.capture(), any(MessagePostProcessor.class));

        assertEquals(replyToQueue, resultQueue.getValue());
        assertEquals(transactions, result.getValue());
        verify(transactionRepository, times(1)).findByUserIdExcludeIncome(userId);
    }

    @Test
    public void testGetTransactionsByUserIdExcludingIncome_NotFound() {
        int userId = 1;
        String replyToQueue = "budget-response";
        String correlationId = UUID.randomUUID().toString();

        when(transactionRepository.findByUserIdExcludeIncome(userId)).thenReturn(Optional.empty());

        assertThrows(TransactionNotFoundException.class, () -> transactionService.getTransactionsByUserIdExcludingIncome(userId, correlationId, replyToQueue));
    }

    @Test
    public void testGetTransactionsByAccountId_Success() {
        int accountId = 1;
        List<Transaction> transactions = Arrays.asList(new Transaction(), new Transaction());
        when(transactionRepository.findByAccountId(accountId)).thenReturn(Optional.of(transactions));

        List<Transaction> result = transactionService.getTransactionsByAccountId(accountId);

        assertEquals(transactions, result);
        verify(transactionRepository, times(1)).findByAccountId(accountId);
    }

    @Test
    public void testGetTransactionsByAccountId_NotFound() {
        int accountId = 1;
        when(transactionRepository.findByAccountId(accountId)).thenReturn(Optional.empty());

        assertThrows(TransactionNotFoundException.class, () -> transactionService.getTransactionsByAccountId(accountId));
    }

    @Test
    public void testGetTransactionsByUserIdAndVendorName_Success() {
        int userId = 1;
        String vendorName = "Vendor";
        List<Transaction> transactions = Arrays.asList(new Transaction(), new Transaction());
        when(transactionRepository.findByUserIdAndVendorName(userId, vendorName)).thenReturn(Optional.of(transactions));

        List<Transaction> result = transactionService.getTransactionsByUserIdAndVendorName(userId, vendorName);

        assertEquals(transactions, result);
        verify(transactionRepository, times(1)).findByUserIdAndVendorName(userId, vendorName);
    }

    @Test
    public void testGetTransactionsByUserIdAndVendorName_NotFound() {
        int userId = 1;
        String vendorName = "Vendor";
        when(transactionRepository.findByUserIdAndVendorName(userId, vendorName)).thenReturn(Optional.empty());

        assertThrows(TransactionNotFoundException.class, () -> transactionService.getTransactionsByUserIdAndVendorName(userId, vendorName));
    }

    @Test
    public void testGetRecentFiveTransactions_Success() {
        int userId = 1;
        List<Transaction> transactions = Arrays.asList(new Transaction(), new Transaction());
        when(transactionRepository.findRecentFiveTransaction(userId)).thenReturn(Optional.of(transactions));

        List<Transaction> result = transactionService.getRecentFiveTransactions(userId);

        assertEquals(transactions, result);
        verify(transactionRepository, times(1)).findRecentFiveTransaction(userId);
    }

    @Test
    public void testGetRecentFiveTransactions_NotFound() {
        int userId = 1;
        when(transactionRepository.findRecentFiveTransaction(userId)).thenReturn(Optional.empty());

        assertThrows(TransactionNotFoundException.class, () -> transactionService.getRecentFiveTransactions(userId));
    }

    @Test
    public void testGetTransactionsFromCurrentMonth_Success() {
        int userId = 1;
        LocalDate currentDate = LocalDate.now();
        List<Transaction> transactions = Arrays.asList(new Transaction(), new Transaction());
        when(transactionRepository.findTransactionFromCurrentMonth(userId, currentDate.getMonthValue(), currentDate.getYear())).thenReturn(Optional.of(transactions));

        List<Transaction> result = transactionService.getTransactionsFromCurrentMonth(userId);

        assertEquals(transactions, result);
        verify(transactionRepository, times(1)).findTransactionFromCurrentMonth(userId, currentDate.getMonthValue(), currentDate.getYear());
    }

    @Test
    public void testCreateTransaction_Success() {
        Transaction transaction = new Transaction();
        transaction.setAccountId(1);
        transaction.setUserId(1);
        transaction.setVendorName("Vendor");
        transaction.setAmount(new BigDecimal("100"));
        transaction.setCategory(TransactionCategory.SHOPPING);
        transaction.setDate(LocalDate.now());
        when(transactionRepository.save(transaction)).thenReturn(transaction);

        Transaction result = transactionService.createTransaction(1, transaction);

        assertEquals(transaction, result);
        verify(transactionRepository, times(1)).save(transaction);
    }

    @Test
    public void testCreateTransaction_InvalidAccountId() {
        Transaction transaction = new Transaction();
        transaction.setAccountId(0);
        transaction.setUserId(1);
        transaction.setVendorName("Vendor");
        transaction.setAmount(new BigDecimal("100"));
        transaction.setCategory(TransactionCategory.SHOPPING);
        transaction.setDate(LocalDate.now());

        assertThrows(InvalidTransactionException.class, () -> transactionService.createTransaction(1, transaction));
    }

    @Test
    public void testCreateTransaction_InvalidUserId() {
        Transaction transaction = new Transaction();
        transaction.setAccountId(1);
        transaction.setUserId(0);
        transaction.setVendorName("Vendor");
        transaction.setAmount(new BigDecimal("100"));
        transaction.setCategory(TransactionCategory.SHOPPING);
        transaction.setDate(LocalDate.now());

        assertThrows(InvalidTransactionException.class, () -> transactionService.createTransaction(0, transaction));
    }

    @Test
    public void testCreateTransaction_InvalidAmount() {
        Transaction transaction = new Transaction();
        transaction.setAccountId(1);
        transaction.setUserId(1);
        transaction.setVendorName("Vendor");
        transaction.setAmount(BigDecimal.ZERO);
        transaction.setCategory(TransactionCategory.SHOPPING);
        transaction.setDate(LocalDate.now());

        assertThrows(InvalidTransactionException.class, () -> transactionService.createTransaction(1, transaction));
    }

    @Test
    public void testCreateTransaction_InvalidNegativeAmount() {
        Transaction transaction = new Transaction();
        transaction.setAccountId(1);
        transaction.setUserId(1);
        transaction.setVendorName("Vendor");
        transaction.setAmount(BigDecimal.valueOf(-7.35));
        transaction.setCategory(TransactionCategory.SHOPPING);
        transaction.setDate(LocalDate.now());

        assertThrows(InvalidTransactionException.class, () -> transactionService.createTransaction(1, transaction));
    }

    @Test
    public void testUpdateTransaction_Success() {
        Transaction transaction = new Transaction();
        transaction.setTransactionId(1);
        transaction.setAccountId(1);
        transaction.setUserId(1);
        transaction.setVendorName("Vendor");
        transaction.setAmount(new BigDecimal("100"));
        transaction.setCategory(TransactionCategory.SHOPPING);
        transaction.setDate(LocalDate.now());

        Transaction existingTransaction = new Transaction();
        existingTransaction.setTransactionId(1);
        existingTransaction.setUserId(1);

        when(transactionRepository.findById(transaction.getTransactionId())).thenReturn(Optional.of(existingTransaction));
        when(transactionRepository.save(existingTransaction)).thenReturn(existingTransaction);

        Transaction result = transactionService.updateTransaction(1, 1, transaction);

        assertEquals(existingTransaction, result);
        verify(transactionRepository, times(1)).findById(transaction.getTransactionId());
        verify(transactionRepository, times(1)).save(existingTransaction);
    }

    @Test
    public void testUpdateTransaction_NotFound() {
        Transaction transaction = new Transaction();
        transaction.setTransactionId(1);
        when(transactionRepository.findById(transaction.getTransactionId())).thenReturn(Optional.empty());

        assertThrows(TransactionNotFoundException.class, () -> transactionService.updateTransaction(1, 1, transaction));
    }

    @Test
    public void testUpdateTransaction_Unauthorized() {
        Transaction transaction = new Transaction();
        transaction.setTransactionId(1);
        transaction.setUserId(2);

        Transaction existingTransaction = new Transaction();
        existingTransaction.setTransactionId(1);
        existingTransaction.setUserId(1);

        when(transactionRepository.findById(transaction.getTransactionId())).thenReturn(Optional.of(existingTransaction));

        assertThrows(ResponseStatusException.class, () -> transactionService.updateTransaction(1, 2, transaction));
    }

    @Test
    public void testDeleteTransaction_Success() {
        int transactionId = 1;
        when(transactionRepository.existsById(transactionId)).thenReturn(true);

        transactionService.deleteTransaction(transactionId);

        verify(transactionRepository, times(1)).deleteById(transactionId);
    }

    @Test
    public void testDeleteTransaction_NotFound() {
        int transactionId = 1;
        when(transactionRepository.existsById(transactionId)).thenReturn(false);

        assertThrows(TransactionNotFoundException.class, () -> transactionService.deleteTransaction(transactionId));
    }

    @Test
    public void testDeleteTransactionByUserId_Success() {
        int userId = 1;

        doNothing().when(transactionRepository).deleteTransactionsByUserId(userId);

        transactionService.deleteTransactionByUserId(userId);

        verify(transactionRepository, times(1)).deleteTransactionsByUserId(userId);
    }

    @Test
    public void testValidateRequestWithHeaders_Success() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("User-ID", "1");

        transactionService.validateRequestWithHeaders(headers);
    }

    @Test
    public void testValidateRequestWithHeaders_UnauthorizedNoHeader() {
        HttpHeaders headers = new HttpHeaders();

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> transactionService.validateRequestWithHeaders(headers));
        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatusCode());
    }

    @Test
    public void testValidateRequestWithHeaders_UnauthorizedInvalidHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("User-ID", "invalid");

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> transactionService.validateRequestWithHeaders(headers));
        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatusCode());
    }
}
