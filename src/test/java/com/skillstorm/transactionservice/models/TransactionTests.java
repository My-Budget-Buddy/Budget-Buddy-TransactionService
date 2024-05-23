package com.skillstorm.transactionservice.models;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

public class TransactionTests {

    private Transaction transaction;

    @BeforeEach
    public void setup() {
        transaction = new Transaction();
    }

    @Test
    public void testSetAndGetTransactionId() {
        int transactionId = 1;
        transaction.setTransactionId(transactionId);
        assertEquals(transactionId, transaction.getTransactionId());
    }

    @Test
    public void testSetAndGetAccountId() {
        int accountId = 1;
        transaction.setAccountId(accountId);
        assertEquals(accountId, transaction.getAccountId());
    }

    @Test
    public void testSetAndGetUserId() {
        int userId = 1;
        transaction.setUserId(userId);
        assertEquals(userId, transaction.getUserId());
    }

    @Test
    public void testSetAndGetVendorName() {
        String vendorName = "Vendor";
        transaction.setVendorName(vendorName);
        assertEquals(vendorName, transaction.getVendorName());
    }

    @Test
    public void testSetAndGetAmount() {
        BigDecimal amount = new BigDecimal("100.00");
        transaction.setAmount(amount);
        assertEquals(amount, transaction.getAmount());
    }

    @Test
    public void testSetAndGetCategory() {
        TransactionCategory category = TransactionCategory.SHOPPING;
        transaction.setCategory(category);
        assertEquals(category, transaction.getCategory());
    }

    @Test
    public void testSetAndGetDate() {
        LocalDate date = LocalDate.now();
        transaction.setDate(date);
        assertEquals(date, transaction.getDate());
    }

    @Test
    public void testSetAndGetDescription() {
        String description = "Sample Description";
        transaction.setDescription(description);
        assertEquals(description, transaction.getDescription());
    }

    @Test
    void testTransactionConstructorAndGetters() {
        int userId = 1;
        int accountId = 2;
        String vendorName = "Test Vendor";
        BigDecimal amount = new BigDecimal("100.00");
        TransactionCategory category = TransactionCategory.GROCERIES;
        String description = "Test Description";
        LocalDate date = LocalDate.now();

        Transaction transaction = new Transaction(userId, accountId, vendorName, amount, category, description, date);

        assertEquals(userId, transaction.getUserId());
        assertEquals(accountId, transaction.getAccountId());
        assertEquals(vendorName, transaction.getVendorName());
        assertEquals(amount, transaction.getAmount());
        assertEquals(category, transaction.getCategory());
        assertEquals(description, transaction.getDescription());
        assertEquals(date, transaction.getDate());
    }

    @Test
    void testSetters() {
        Transaction transaction = new Transaction();

        int transactionId = 1;
        int userId = 1;
        int accountId = 2;
        String vendorName = "Test Vendor";
        BigDecimal amount = new BigDecimal("100.00");
        TransactionCategory category = TransactionCategory.GROCERIES;
        String description = "Test Description";
        LocalDate date = LocalDate.now();

        transaction.setTransactionId(transactionId);
        transaction.setUserId(userId);
        transaction.setAccountId(accountId);
        transaction.setVendorName(vendorName);
        transaction.setAmount(amount);
        transaction.setCategory(category);
        transaction.setDescription(description);
        transaction.setDate(date);

        assertEquals(transactionId, transaction.getTransactionId());
        assertEquals(userId, transaction.getUserId());
        assertEquals(accountId, transaction.getAccountId());
        assertEquals(vendorName, transaction.getVendorName());
        assertEquals(amount, transaction.getAmount());
        assertEquals(category, transaction.getCategory());
        assertEquals(description, transaction.getDescription());
        assertEquals(date, transaction.getDate());
    }

    @Test
    void testEquals() {
        LocalDate date = LocalDate.now();
        Transaction transaction1 = new Transaction(1, 2, "Vendor", new BigDecimal("100.00"), TransactionCategory.GROCERIES, "Description", date);
        Transaction transaction2 = new Transaction(1, 2, "Vendor", new BigDecimal("100.00"), TransactionCategory.GROCERIES, "Description", date);

        assertEquals(transaction1, transaction2);

        transaction2.setTransactionId(2);

        assertNotEquals(transaction1, transaction2);
    }

    @Test
    void testHashCode() {
        LocalDate date = LocalDate.now();
        Transaction transaction1 = new Transaction(1, 2, "Vendor", new BigDecimal("100.00"), TransactionCategory.GROCERIES, "Description", date);
        Transaction transaction2 = new Transaction(1, 2, "Vendor", new BigDecimal("100.00"), TransactionCategory.GROCERIES, "Description", date);

        assertEquals(transaction1.hashCode(), transaction2.hashCode());

        transaction2.setTransactionId(2);

        assertNotEquals(transaction1.hashCode(), transaction2.hashCode());
    }

    @Test
    void testToString() {
        LocalDate date = LocalDate.now();
        Transaction transaction = new Transaction(1, 2, "Vendor", new BigDecimal("100.00"), TransactionCategory.GROCERIES, "Description", date);
        String expected = "Transaction{transactionId=0, userId=1, accountId=2, vendorName='Vendor', amount=100.00, category='Groceries', description='Description', date=" + date + "}";

        assertEquals(expected, transaction.toString());
    }
}

