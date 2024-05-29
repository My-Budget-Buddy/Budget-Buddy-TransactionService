package com.skillstorm.transactionservice.models;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class TransactionCategorySerializer extends StdSerializer<TransactionCategory> {

    public TransactionCategorySerializer() {
        super(TransactionCategory.class);
    }

    @Override
    public void serialize(TransactionCategory value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        //uses the Transaction Category's toString method
        gen.writeString(value.toString());
    }
}

