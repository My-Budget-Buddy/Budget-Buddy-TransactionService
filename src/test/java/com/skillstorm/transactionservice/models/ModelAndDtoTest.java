package com.skillstorm.transactionservice.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.meanbean.test.BeanVerifier;

public class ModelAndDtoTest {

    Transaction arbitraryTransaction;

    @Test
    public void ModelAndDtoAllTest() {

        BeanVerifier.verifyBean(Transaction.class);
    }

    @Test
    public void equalNullTest() {
        arbitraryTransaction = null;
        Transaction transaction1 = new Transaction(1, 1, "name", BigDecimal.valueOf(100), TransactionCategory.GROCERIES,
                "description", null);
        transaction1.equals(arbitraryTransaction);
        assertNotEquals(transaction1, arbitraryTransaction);
    }

    @Test
    public void equalObjectsTest() {
        LocalDate date = LocalDate.now();
        Transaction transaction1 = new Transaction(1, 1, "name", BigDecimal.valueOf(100), TransactionCategory.GROCERIES,
                "description", date);
        Transaction transaction2 = new Transaction(1, 1, "name", BigDecimal.valueOf(100), TransactionCategory.GROCERIES,
                "description", date);

        assertEquals(transaction1, transaction2);
    }
}
