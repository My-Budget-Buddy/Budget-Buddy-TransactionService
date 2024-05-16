package com.skillstorm.transactionservice.services;

import com.skillstorm.transactionservice.exceptions.TransactionNotFoundException;
import com.skillstorm.transactionservice.models.Transaction;
import com.skillstorm.transactionservice.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    // Get a list of transactions by userId
    public List<Transaction> getTransactionByUserId(int userId) {
        return transactionRepository.findByUserId(userId)
                .orElseThrow(() -> new TransactionNotFoundException("Transactions for user ID " + userId + " not found"));
    }

    // Get a transaction by transactionId
    public Transaction getTransactionById(int transactionId) {
        return transactionRepository.findById(transactionId)
                .orElseThrow(() -> new TransactionNotFoundException("Transaction with ID " + transactionId + " not found"));
    }

    // Get transactions by accountId
    public List<Transaction> getTransactionByAccountId(int accountId) {
        return transactionRepository.findByAccountId(accountId)
                .orElseThrow(() -> new TransactionNotFoundException("Transactions for account ID " + accountId + " not found"));
    }

    // Get transactions by vendor name
    public List<Transaction> getTransactionByVendorName(String vendorName) {
        return transactionRepository.findByVendorName(vendorName)
                .orElseThrow(() -> new TransactionNotFoundException("Transactions for vendor " + vendorName + " not found"));
    }

    // Get transactions from the last 7 days
    public List<Transaction> getTransactionFromLast7Days() {
        LocalDate date = LocalDate.now().minusDays(7);
        return transactionRepository.findFromLast7Days(date)
                .orElseThrow(() -> new TransactionNotFoundException("No transactions found from the last 7 days"));
    }

    // Get transactions by category
    public List<Transaction> getTransactionByCategory(String category) {
        return transactionRepository.findByCategory(category)
                .orElseThrow(() -> new TransactionNotFoundException("Transactions for category " + category + " not found"));
    }

    // Create a transaction
    public Transaction createTransaction(Transaction transaction) {
        // You can add custom validation logic here and throw InvalidTransactionException if needed
        return transactionRepository.save(transaction);
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
        if (transaction.getVendorName() != null) {
            existingTransaction.setVendorName(transaction.getVendorName());
        }
        if (transaction.getAmount() != 0) {
            existingTransaction.setAmount(transaction.getAmount());
        }
        if (transaction.getCategory() != null) {
            existingTransaction.setCategory(transaction.getCategory());
        }
        if (transaction.getDescription() != null) {
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
