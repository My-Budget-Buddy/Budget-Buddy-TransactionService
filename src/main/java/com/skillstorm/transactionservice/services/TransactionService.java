package com.skillstorm.transactionservice.services;

import com.skillstorm.transactionservice.exceptions.InvalidTransactionException;
import com.skillstorm.transactionservice.exceptions.TransactionNotFoundException;
import com.skillstorm.transactionservice.models.Transaction;
import com.skillstorm.transactionservice.repositories.TransactionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    // Get a list of transactions by userId
    public List<Transaction> getTransactionByUserId(int userId) {
        Optional<List<Transaction>> transactionList = transactionRepository.findByUserId(userId);
        if (transactionList.isEmpty() || transactionList.get().isEmpty()){
            throw new TransactionNotFoundException("Transactions for user ID " + userId + " not found"); 
        } else {
            return transactionList.get();
        }       
    }

    // Get a transaction by transactionId
    public Transaction getTransactionById(int transactionId) {
        Optional<Transaction> transaction = transactionRepository.findById(transactionId);
        if (transaction.isPresent()){
            return transaction.get();
        } else {
            throw new TransactionNotFoundException("Transaction with ID " + transactionId + " not found");
        }
    }

    // Get transactions by accountId
    public List<Transaction> getTransactionByAccountId(int accountId) {
        Optional<List<Transaction>> transactionList = transactionRepository.findByAccountId(accountId);
        if (transactionList.isEmpty() || transactionList.get().isEmpty()) {
            throw new TransactionNotFoundException("Transactions for account ID " + accountId + " not found");
        } else {
            return transactionList.get();
        }
    }

    // Get transactions by vendor name
    public List<Transaction> getTransactionByVendorName(String vendorName) {
        Optional<List<Transaction>> transactionList = transactionRepository.findByVendorName(vendorName);
        if (transactionList.isEmpty() || transactionList.get().isEmpty()) {
            throw new TransactionNotFoundException("Transactions for vendor " + vendorName + " not found");
        } else {
            return transactionList.get();
        }
    }

    // Get transactions from the last 7 days
    public List<Transaction> getTransactionFromLast7Days() {
        LocalDate date = LocalDate.now().minusDays(7);
        Optional<List<Transaction>> transactionList = transactionRepository.findFromLast7Days(date);
        if (transactionList.isEmpty() || transactionList.get().isEmpty()) {
            throw new TransactionNotFoundException("No transactions found from the last 7 days");
        } else {
            return transactionList.get();
        }
    }

    // Get transactions by category
    public List<Transaction> getTransactionByCategory(String category) {
        Optional<List<Transaction>> transactionList = transactionRepository.findByCategory(category);
        if (transactionList.isEmpty() || transactionList.get().isEmpty()) {
            throw new TransactionNotFoundException("Transactions for category " + category + " not found");
        } else {
            return transactionList.get();
        }
    }

    // Create a transaction
    public Transaction createTransaction(Transaction transaction) {
        Transaction newTransaction = transactionRepository.save(transaction); 
        if (newTransaction == null) {
            return newTransaction;
        } else {
            throw new InvalidTransactionException("The transaction entered was invalid.");
        }
    }

    // Update a transaction
    public Transaction updateTransaction(Transaction transaction) {
        Transaction existingTransaction = transactionRepository.findById(transaction.getTransactionId())
                .orElseThrow(() -> new TransactionNotFoundException("Transaction with ID " + transaction.getTransactionId() + " not found"));

        if (transaction.getUserId() != 0) {
            existingTransaction.setUserId(transaction.getUserId());
        }
        if (transaction.getAccountId() != 0) {
            existingTransaction.setAccountId(transaction.getAccountId());
        }
        if (transaction.getVendorName() != "") {
            existingTransaction.setVendorName(transaction.getVendorName());
        }
        if (transaction.getAmount() != 0) {
            existingTransaction.setAmount(transaction.getAmount());
        }
        if (transaction.getCategory() != "") {
            existingTransaction.setCategory(transaction.getCategory());
        }
        if (transaction.getDescription() != "") {
            existingTransaction.setDescription(transaction.getDescription());
        }
        if (transaction.getDate() != null) {
            existingTransaction.setDate(transaction.getDate());
        }
        return transactionRepository.save(existingTransaction);
    }

    // Delete a transaction
    public void deleteTransaction(int transactionId) {
        if (!transactionRepository.existsById(transactionId)) {
            throw new TransactionNotFoundException("Transaction with ID " + transactionId + " not found");
        }
        transactionRepository.deleteById(transactionId);
    }
}
