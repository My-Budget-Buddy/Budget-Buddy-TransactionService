package com.skillstorm.transactionservice.controllers;

import com.skillstorm.transactionservice.models.Transaction;
import com.skillstorm.transactionservice.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    //Mapping for getting all transactions by userId
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Transaction>> getTransactionsByUserId(@PathVariable int userId) {
        List<Transaction> transactionsList = transactionService.getTransactionByUserId(userId);
        return new ResponseEntity<>(transactionsList, HttpStatus.OK);
    }

    //Mapping for getting creating a transaction
    @PostMapping("/createTransaction")
    public ResponseEntity<Transaction> createTransaction(@RequestBody Transaction transaction) {
        Transaction newTransaction = transactionService.createTransaction(transaction);
        return new ResponseEntity<>(newTransaction, HttpStatus.CREATED);
    }

    //Mapping for update a transaction
    @PutMapping("/updateTransaction")
    public ResponseEntity<Transaction> updateTransaction(@RequestBody Transaction transaction) {
        Transaction updatedTransaction = transactionService.updateTransaction(transaction);
        return new ResponseEntity<>(updatedTransaction, HttpStatus.OK);
    }

    //Mapping for deleting a transaction
    @DeleteMapping("/deleteTransaction/{transactionId}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable int transactionId) {
        transactionService.deleteTransaction(transactionId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
