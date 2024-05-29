package com.skillstorm.transactionservice.models;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;

public class TransactionCategoryDeserializer extends JsonDeserializer<TransactionCategory> {

    @Override
    public TransactionCategory deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        //get the text from JSON parser, replace spaces with underscores, and convert to uppercase
        String value = p.getText().replace(' ', '_').toUpperCase();
        return TransactionCategory.valueOf(value);
    }
}
