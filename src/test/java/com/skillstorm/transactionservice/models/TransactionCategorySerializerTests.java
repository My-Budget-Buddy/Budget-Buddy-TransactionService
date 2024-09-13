package com.skillstorm.transactionservice.models;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;


@ExtendWith(MockitoExtension.class)
public class TransactionCategorySerializerTests {

    @Mock
    TransactionCategory transactionCategory;
    @Mock
    JsonGenerator generator;
    @Mock
    SerializerProvider serializerProvider;
    @InjectMocks
    TransactionCategorySerializer transactionCategorySerializer;

    /*
     * TransactionCategory has a valid value
     */
    @Test
    void testSerialize(){
        
        String str = "GROCERIES";
        try{
            when(transactionCategory.toString()).thenReturn(str);
            doNothing().when(generator).writeString(anyString());
            transactionCategorySerializer.serialize(transactionCategory, generator, serializerProvider);
            verify(generator).writeString(anyString());
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    /*
     * TransactionCategory has a null value
     */
    @Test
    void testSerializeNull(){
        
        String str = null;
        try{
            when(transactionCategory.toString()).thenReturn(str);
            doNothing().when(generator).writeString(str);
            transactionCategorySerializer.serialize(transactionCategory, generator, serializerProvider);
            verify(generator).writeString(str);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

}
