package com.skillstorm.transactionservice.models;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;

public class TransactionCategoryDeserializer extends JsonDeserializer<TransactionCategory> {

    @Override
    public TransactionCategory deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String value = p.getText().replace(' ', '_').toUpperCase();
        return TransactionCategory.valueOf(value);
    }
}
