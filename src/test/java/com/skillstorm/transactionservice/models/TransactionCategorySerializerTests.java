package com.skillstorm.transactionservice.models;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.io.StringWriter;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.ser.impl.WritableObjectId;

public class TransactionCategorySerializerTests {

    TransactionCategorySerializer transactionCategorySerializer;
    SerializerProvider serializerProvider;

    @Test
    void testSerializeSuccess() throws IOException {
        // Arrange
        transactionCategorySerializer = new TransactionCategorySerializer();
        TransactionCategory cate = TransactionCategory.ENTERTAINMENT;
        JsonFactory factory = new JsonFactory();
        StringWriter jsonObjectWriter = new StringWriter();
        JsonGenerator generator = factory.createGenerator(jsonObjectWriter);
        // Assert it does not throw an exception with good values
        assertDoesNotThrow(() -> {
            // Act
            transactionCategorySerializer.serialize(cate, generator, serializerProvider);
        });
    }
}
