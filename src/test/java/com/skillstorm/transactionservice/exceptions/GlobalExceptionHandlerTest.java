package com.skillstorm.transactionservice.exceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class GlobalExceptionHandlerTest {
    private GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();


    @Test
    public void testTransactionNotFoundException() {
        TransactionNotFoundException exception = new TransactionNotFoundException("Transaction not found");

        ResponseEntity<String> expectedResponse = new ResponseEntity<>("Transaction not found", HttpStatus.NOT_FOUND);

        ResponseEntity<String> actualResponse = globalExceptionHandler.handleTransactionNotFoundException(exception);

        String expectedMessage = expectedResponse.getBody();
        String actualMessage = actualResponse.getBody();

        assertEquals(expectedMessage, actualMessage, "Response body should match the expected message");
        assertEquals(expectedResponse.getStatusCode(), actualResponse.getStatusCode(), "HTTP status code should match");
    }

    @Test
    public void testInvalidTransactionException() {
        InvalidTransactionException exception = new InvalidTransactionException("Transaction is not valid");

        ResponseEntity<String> expectedResponse = new ResponseEntity<>("Transaction is not valid", HttpStatus.BAD_REQUEST);

        ResponseEntity<String> actualResponse = globalExceptionHandler.handleInvalidTransactionException(exception);

        String expectedMessage = expectedResponse.getBody();
        String actualMessage = actualResponse.getBody();

        assertEquals(expectedMessage, actualMessage, "Response body should match the expected message");
        assertEquals(expectedResponse.getStatusCode(), actualResponse.getStatusCode(), "HTTP status code should match");
    }
}
