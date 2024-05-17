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

    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    // Mapping for getting all transactions by userId
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Transaction>> getTransactionsByUserId(@PathVariable int userId) {
        List<Transaction> transactionsList = transactionService.getTransactionByUserId(userId);
        return new ResponseEntity<>(transactionsList, HttpStatus.OK);
    }

    //Mapping for getting most recent 5 transactions
    @GetMapping("/recentTransactions/{userId}")
    public ResponseEntity<List<Transaction>> getRecentFiveTransaction(@PathVariable int userId){
        List<Transaction> transactionsList = transactionService.getRecentFiveTransaction(userId);
        return new ResponseEntity<>(transactionsList, HttpStatus.OK);
    }

    //Mapping for getting transaction for the current month
    @GetMapping("/currentMonthTransaction/{userId}")
    public ResponseEntity<List<Transaction>> getTransactionFromCurrentMonth(@PathVariable int userId){
        List<Transaction> transactionsList = transactionService.getTransactionFromCurrentMonth(userId);
        return new ResponseEntity<>(transactionsList, HttpStatus.OK);
    }

    //Mapping for getting all transaction by userId that excludes the transaction category INCOME used for communication with Budgets service
    @GetMapping("/budgets/{userId}")
    public ResponseEntity<List<Transaction>> getTransactionByUserIdExcludeIncome(@PathVariable int userId){
        List<Transaction> transactionsList = transactionService.getTransactionByUserIdExcludingIncome(userId);
        return  new ResponseEntity<>(transactionsList, HttpStatus.OK);
    }

    //Mapping for getting all transactions by accountId
    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<Transaction>> getTransactionsByAccountId(@PathVariable int accountId){
        List<Transaction> transactionsList = transactionService.getTransactionByAccountId(accountId);
        return new ResponseEntity<>(transactionsList, HttpStatus.OK);
    }

    // Mapping for getting a transaction by transactionId
//    @GetMapping("/{transactionId}")
//    public ResponseEntity<Transaction> getTransactionById(@PathVariable int transactionId) {
//        Transaction transaction = transactionService.getTransactionById(transactionId);
//        return new ResponseEntity<>(transaction, HttpStatus.OK);
//    }

    // Mapping for creating a transaction
    @PostMapping("/createTransaction")
    public ResponseEntity<Transaction> createTransaction(@RequestBody Transaction transaction) {
        Transaction newTransaction = transactionService.createTransaction(transaction);
        return new ResponseEntity<>(newTransaction, HttpStatus.CREATED);
    }

    // Mapping for updating a transaction
    @PutMapping("/updateTransaction")
    public ResponseEntity<Transaction> updateTransaction(@RequestBody Transaction transaction) {
        Transaction updatedTransaction = transactionService.updateTransaction(transaction);
        return new ResponseEntity<>(updatedTransaction, HttpStatus.OK);
    }

    // Mapping for deleting a transaction
    @DeleteMapping("/deleteTransaction/{transactionId}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable int transactionId) {
        transactionService.deleteTransaction(transactionId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
