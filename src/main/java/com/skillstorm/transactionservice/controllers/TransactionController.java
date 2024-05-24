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
@CrossOrigin
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    // Mapping for getting all transactions by userId
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Transaction>> getTransactionsByUserId(@PathVariable int userId, @RequestHeader HttpHeaders headers) {
        transactionService.validateUserId(userId, headers);
        List<Transaction> transactionsList = transactionService.getTransactionsByUserId(userId);
        return new ResponseEntity<>(transactionsList, HttpStatus.OK);
    }

    //Mapping for getting most recent 5 transactions
    @GetMapping("/recentTransactions/{userId}")
    public ResponseEntity<List<Transaction>> getRecentFiveTransactions(@PathVariable int userId, @RequestHeader HttpHeaders headers){
        transactionService.validateUserId(userId, headers);
        List<Transaction> transactionsList = transactionService.getRecentFiveTransactions(userId);
        return new ResponseEntity<>(transactionsList, HttpStatus.OK);
    }

    //Mapping for getting transaction for the current month
    @GetMapping("/currentMonthTransactions/{userId}")
    public ResponseEntity<List<Transaction>> getTransactionsFromCurrentMonth(@PathVariable int userId, @RequestHeader HttpHeaders headers){
        transactionService.validateUserId(userId, headers);
        List<Transaction> transactionsList = transactionService.getTransactionsFromCurrentMonth(userId);
        return new ResponseEntity<>(transactionsList, HttpStatus.OK);
    }

    //Mapping for getting all transactions by vendorName and userId
    @GetMapping("/user/{userId}/vendor/{vendorName}")
    public ResponseEntity<List<Transaction>> getTransactionsByUserIdAndVendorName(@PathVariable int userId, @PathVariable String vendorName, @RequestHeader HttpHeaders headers) {
        transactionService.validateUserId(userId, headers);
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
    @PostMapping("/user/{userId}/createTransaction")
    public ResponseEntity<Transaction> createTransaction(@PathVariable int userId, @RequestBody Transaction transaction, @RequestHeader HttpHeaders headers) {
        transactionService.validateUserId(userId, headers);
        Transaction newTransaction = transactionService.createTransaction(transaction);
        return new ResponseEntity<>(newTransaction, HttpStatus.CREATED);
    }

    // Mapping for updating a transaction
    @PutMapping("/user/{userId}/updateTransaction")
    public ResponseEntity<Transaction> updateTransaction(@PathVariable int userId, @RequestBody Transaction transaction, @RequestHeader HttpHeaders headers) {
        transactionService.validateUserId(userId, headers);
        Transaction updatedTransaction = transactionService.updateTransaction(transaction);
        return new ResponseEntity<>(updatedTransaction, HttpStatus.OK);
    }

    // Mapping for deleting a transaction with transaction Id
    @DeleteMapping("/user/{userId}/deleteTransaction/{transactionId}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable int userId, @PathVariable int transactionId, @RequestHeader HttpHeaders headers) {
        transactionService.validateUserId(userId, headers);
        transactionService.deleteTransaction(transactionId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
