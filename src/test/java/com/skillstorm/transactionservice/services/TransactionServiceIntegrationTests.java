package com.skillstorm.transactionservice.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.skillstorm.transactionservice.exceptions.InvalidTransactionException;
import com.skillstorm.transactionservice.exceptions.TransactionNotFoundException;
import com.skillstorm.transactionservice.models.Transaction;
import com.skillstorm.transactionservice.models.TransactionCategory;
import com.skillstorm.transactionservice.repositories.TransactionRepository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


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
        Transaction transaction1 = new Transaction(userId, 1, "Vendor A", new BigDecimal("100"), TransactionCategory.SHOPPING, "Description A", LocalDate.now());
        Transaction transaction2 = new Transaction(userId, 1, "Vendor B", new BigDecimal("200"), TransactionCategory.SHOPPING, "Description B", LocalDate.now());
        transactionRepository.save(transaction1);
        transactionRepository.save(transaction2);

        List<Transaction> result = transactionService.getTransactionsByUserId(userId);

        assertThat(result).containsExactlyInAnyOrder(transaction1, transaction2);
    }

    @Test
    public void testGetTransactionsByUserId_NotFound() {
        int userId = 1;
        assertThrows(TransactionNotFoundException.class, () -> transactionService.getTransactionsByUserId(userId));
    }

    @Test
    public void testGetTransactionsByUserIdExcludingIncome_Success() {
        int userId = 1;
        Transaction transaction1 = new Transaction(userId, 1, "Vendor A", new BigDecimal("100"), TransactionCategory.SHOPPING, "Description A", LocalDate.now());
        Transaction transaction2 = new Transaction(userId, 1, "Vendor B", new BigDecimal("200"), TransactionCategory.INCOME, "Description B", LocalDate.now());
        transactionRepository.save(transaction1);
        transactionRepository.save(transaction2);

        List<Transaction> result = transactionService.getTransactionsByUserIdExcludingIncome(userId);

        assertThat(result).containsExactly(transaction1);
    }

    @Test
    public void testGetTransactionsByUserIdExcludingIncome_NotFound() {
        int userId = 1;
        assertThrows(TransactionNotFoundException.class, () -> transactionService.getTransactionsByUserIdExcludingIncome(userId));
    }

    @Test
    public void testGetTransactionsByAccountId_Success() {
        int accountId = 1;
        Transaction transaction1 = new Transaction(1, accountId, "Vendor A", new BigDecimal("100"), TransactionCategory.SHOPPING, "Description A", LocalDate.now());
        Transaction transaction2 = new Transaction(1, accountId, "Vendor B", new BigDecimal("200"), TransactionCategory.SHOPPING, "Description B", LocalDate.now());
        transactionRepository.save(transaction1);
        transactionRepository.save(transaction2);

        List<Transaction> result = transactionService.getTransactionsByAccountId(accountId);

        assertThat(result).containsExactlyInAnyOrder(transaction1, transaction2);
    }

    @Test
    public void testGetTransactionsByAccountId_NotFound() {
        int accountId = 1;
        assertThrows(TransactionNotFoundException.class, () -> transactionService.getTransactionsByAccountId(accountId));
    }

    @Test
    public void testGetRecentFiveTransactions_Success() {
        int userId = 1;
        Transaction transaction1 = new Transaction(userId, 1, "Vendor A", new BigDecimal("100"), TransactionCategory.SHOPPING, "Description A", LocalDate.now());
        Transaction transaction2 = new Transaction(userId, 1, "Vendor B", new BigDecimal("200"), TransactionCategory.SHOPPING, "Description B", LocalDate.now().minusDays(1));
        transactionRepository.save(transaction1);
        transactionRepository.save(transaction2);

        List<Transaction> result = transactionService.getRecentFiveTransactions(userId);

        assertThat(result).containsExactly(transaction1, transaction2);
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
        Transaction transaction1 = new Transaction(userId, 1, "Vendor A", new BigDecimal("100"), TransactionCategory.SHOPPING, "Description A", currentDate);
        Transaction transaction2 = new Transaction(userId, 1, "Vendor B", new BigDecimal("200"), TransactionCategory.SHOPPING, "Description B", currentDate.minusMonths(1));
        transactionRepository.save(transaction1);
        transactionRepository.save(transaction2);

        List<Transaction> result = transactionService.getTransactionsFromCurrentMonth(userId);

        assertThat(result).containsExactly(transaction1);
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

        Transaction result = transactionService.createTransaction(1, transaction);

        assertThat(result).isNotNull();
        assertThat(result.getTransactionId()).isNotNull();
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
    public void testUpdateTransaction_Success() {
        Transaction transaction = new Transaction();
        transaction.setAccountId(1);
        transaction.setUserId(1);
        transaction.setVendorName("Vendor");
        transaction.setAmount(new BigDecimal("100"));
        transaction.setCategory(TransactionCategory.SHOPPING);
        transaction.setDate(LocalDate.now());
        transaction = transactionRepository.save(transaction);

        transaction.setVendorName("Updated Vendor");
        Transaction result = transactionService.updateTransaction(1, 1, transaction);

        assertThat(result.getVendorName()).isEqualTo("Updated Vendor");
    }

    @Test
    public void testUpdateTransaction_NotFound() {
        Transaction transaction = new Transaction();
        transaction.setTransactionId(1);

        assertThrows(TransactionNotFoundException.class, () -> transactionService.updateTransaction(4, 1, transaction));
    }

    @Test
    public void testDeleteTransaction_Success() {
        Transaction transaction = new Transaction();
        transaction.setAccountId(1);
        transaction.setUserId(1);
        transaction.setVendorName("Vendor");
        transaction.setAmount(new BigDecimal("100"));
        transaction.setCategory(TransactionCategory.SHOPPING);
        transaction.setDate(LocalDate.now());
        transaction = transactionRepository.save(transaction);

        transactionService.deleteTransaction(transaction.getTransactionId());

        assertThat(transactionRepository.existsById(transaction.getTransactionId())).isFalse();
    }

    @Test
    public void testDeleteTransaction_NotFound() {
        int transactionId = 1;
        assertThrows(TransactionNotFoundException.class, () -> transactionService.deleteTransaction(transactionId));
    }
}