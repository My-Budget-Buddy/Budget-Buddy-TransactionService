package com.skillstorm.transactionservice.models;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyChar;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;

public class TransactionCategoryDeserializerTests {

    @Mock
    JsonParser parser;
    @Mock
    DeserializationContext deseralizationContext;
    @Mock
    String string;
    @InjectMocks
    TransactionCategoryDeserializer categoryDeserializer;

    @Test
    void testDeserialize() {

        try {
            // Arrange
            when(parser.getText()).thenReturn("GROCERIES");
            doNothing().when(string).replace(anyChar(), anyChar());
            doNothing().when(string).toUpperCase();
            assertDoesNotThrow(() -> {
                // Act
                TransactionCategory actual = categoryDeserializer.deserialize(parser, deseralizationContext);
                // Assert
                assertEquals("GROCERIES", actual);
            });

        } catch (Exception e) {
            // IO Exception
        }
    }
}
