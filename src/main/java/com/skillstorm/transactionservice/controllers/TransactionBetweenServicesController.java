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
@RequestMapping("/transactionsPrivate")
public class TransactionBetweenServicesController {

    private final TransactionService transactionService;

    @Autowired
    public TransactionBetweenServicesController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    // Mapping for getting all transactions by userId
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Transaction>> getTransactionsByUserId(@PathVariable int userId) {
        List<Transaction> transactionsList = transactionService.getTransactionsByUserId(userId);
        return new ResponseEntity<>(transactionsList, HttpStatus.OK);
    }

    /*
        Mapping for getting all transaction by userId that excludes the transaction category INCOME
        This endpoint is used by the Budget Service
     */
    @GetMapping("/budget/{userId}")
    public ResponseEntity<List<Transaction>> getTransactionsByUserIdExcludeIncome(@PathVariable int userId){
        List<Transaction> transactionsList = transactionService.getTransactionsByUserIdExcludingIncome(userId);
        return  new ResponseEntity<>(transactionsList, HttpStatus.OK);
    }

    //Mapping for getting all transactions by accountId
    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<Transaction>> getTransactionsByAccountId(@PathVariable int accountId){
        List<Transaction> transactionsList = transactionService.getTransactionsByAccountId(accountId);
        return new ResponseEntity<>(transactionsList, HttpStatus.OK);
    }

    // Mapping for deleting a transaction with userId
    @DeleteMapping("/deleteTransaction/user/{userId}")
    public ResponseEntity<Void> deleteTransactionByUserId(@PathVariable int userId){
        transactionService.deleteTransactionByUserId(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
