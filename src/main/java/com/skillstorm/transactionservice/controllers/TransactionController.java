package com.skillstorm.transactionservice.controllers;

import com.skillstorm.transactionservice.models.Transaction;
import com.skillstorm.transactionservice.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    // Mapping for getting all transactions by userId
    @GetMapping
    public ResponseEntity<List<Transaction>> getTransactionsByUserId(@RequestHeader HttpHeaders headers) {
        transactionService.validateRequestWithHeaders(headers);

        // the validation function should catch any errors by this point, so this is safe
        int userId = Integer.parseInt(headers.getFirst("User-ID"));

        List<Transaction> transactionsList = transactionService.getTransactionsByUserId(userId);
        return new ResponseEntity<>(transactionsList, HttpStatus.OK);
    }

    // Mapping for getting most recent 5 transactions
    @GetMapping("/recentTransactions")
    public ResponseEntity<List<Transaction>> getRecentFiveTransactions(@RequestHeader HttpHeaders headers) {
        transactionService.validateRequestWithHeaders(headers);

        int userId = Integer.parseInt(headers.getFirst("User-ID"));

        List<Transaction> transactionsList = transactionService.getRecentFiveTransactions(userId);
        return new ResponseEntity<>(transactionsList, HttpStatus.OK);
    }

    // Mapping for getting transaction for the current month
    @GetMapping("/currentMonthTransactions")
    public ResponseEntity<List<Transaction>> getTransactionsFromCurrentMonth(@RequestHeader HttpHeaders headers) {
        transactionService.validateRequestWithHeaders(headers);

        int userId = Integer.parseInt(headers.getFirst("User-ID"));

        List<Transaction> transactionsList = transactionService.getTransactionsFromCurrentMonth(userId);
        return new ResponseEntity<>(transactionsList, HttpStatus.OK);
    }

    // Mapping for getting all transactions by vendorName and userId
    @GetMapping("/vendor/{vendorName}")
    public ResponseEntity<List<Transaction>> getTransactionsByUserIdAndVendorName(@PathVariable String vendorName, @RequestHeader HttpHeaders headers) {
        transactionService.validateRequestWithHeaders(headers);

        int userId = Integer.parseInt(headers.getFirst("User-ID"));
        
        List<Transaction> transactionsList = transactionService.getTransactionsByUserIdAndVendorName(userId, vendorName);
        return new ResponseEntity<>(transactionsList, HttpStatus.OK);
    }

    // Mapping for getting a transaction by transactionId
//    @GetMapping("/{transactionId}")
//    public ResponseEntity<Transaction> getTransactionById(@PathVariable int transactionId) {
//        Transaction transaction = transactionService.getTransactionById(transactionId);
//        return new ResponseEntity<>(transaction, HttpStatus.OK);
//    }

    // Mapping for creating a transaction
    @PostMapping
    public ResponseEntity<Transaction> createTransaction(@RequestBody Transaction transaction, @RequestHeader HttpHeaders headers) {
        transactionService.validateRequestWithHeaders(headers);

        int userId = Integer.parseInt(headers.getFirst("User-ID"));
        
        Transaction newTransaction = transactionService.createTransaction(userId, transaction);
        return new ResponseEntity<>(newTransaction, HttpStatus.CREATED);
    }

    // Mapping for updating a transaction
    @PutMapping("/{transactionId}")
    public ResponseEntity<Transaction> updateTransaction(@PathVariable int transactionId, @RequestBody Transaction transaction, @RequestHeader HttpHeaders headers) {
        transactionService.validateRequestWithHeaders(headers);

        int userId = Integer.parseInt(headers.getFirst("User-ID"));
        
        Transaction updatedTransaction = transactionService.updateTransaction(transactionId, userId, transaction);
        return new ResponseEntity<>(updatedTransaction, HttpStatus.OK);
    }

    // Mapping for deleting a transaction with transaction Id
    @DeleteMapping("/{transactionId}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable int transactionId, @RequestHeader HttpHeaders headers) {
        transactionService.validateRequestWithHeaders(headers);
        transactionService.deleteTransaction(transactionId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
