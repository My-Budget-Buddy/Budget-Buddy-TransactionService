package com.skillstorm.transactionservice.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;

@ExtendWith(MockitoExtension.class)
public class TransactionCategoryDeserializerTests {

    @Mock
    JsonParser parser;
    @Mock
    DeserializationContext deseralizationContext;
    @InjectMocks
    TransactionCategoryDeserializer categoryDeserializer;

    /*
     * If JSON PARSER has a Valid TransactionCategory value used it should return a valid TransactionCategory
     * Example of all current valid TransactionCategory listed below:
     * GROCERIES,ENTERTAINMENT,DINING,TRANSPORTATION,HEALTHCARE,LIVING_EXPENSES,SHOPPING,INCOME,MISC;
     */
    @Test
    void testDeserializeAllValidValues() {

        // Arrange
        // Retrieve all valid enums
        TransactionCategory[] allValidCategories = TransactionCategory.values();

        // Test all valid enums through iteration
        for (TransactionCategory validCategory : allValidCategories) {
            try {
                // Arrange
                when(parser.getText()).thenReturn(validCategory.name());
                // Act
                TransactionCategory actual = categoryDeserializer.deserialize(parser, deseralizationContext);
                // Assert
                assertEquals(validCategory.name(), actual.name());

            } catch (Exception e) {
                // IO Exception
            }
        }
    }

    /*
     * If JSON Parser has a null value we should expect a null pointer exception
     * from the deserialize method.
     */
    @Test
    void testDeserializeWithNull() {

        try {
            // Arrange
            when(parser.getText()).thenReturn(null);
            // Assert
            assertThrows(NullPointerException.class, () -> {
                // Act
                categoryDeserializer.deserialize(parser, deseralizationContext);
            });

        } catch (Exception e) {
            // IO Exception
        }
    }

    /*
     * If JSON Parser has a Invalid TransactionCategory value then we should expect
     * a illegal argument exception from the deserialize method.
     */
    @Test
    void testDeserializeWithIllegalArgumentException() {

        try {
            // Arrange
            when(parser.getText()).thenReturn("Invalid_Transaction.Category");
            assertThrows(IllegalArgumentException.class, () -> {
                // Act
                categoryDeserializer.deserialize(parser, deseralizationContext);
            });

        } catch (Exception e) {
            // IO Exception
        }
    }

}
