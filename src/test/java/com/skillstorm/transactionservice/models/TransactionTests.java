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
}

