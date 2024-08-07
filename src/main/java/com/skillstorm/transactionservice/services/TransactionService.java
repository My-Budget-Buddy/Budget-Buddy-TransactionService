package com.skillstorm.transactionservice.services;

import com.skillstorm.transactionservice.exceptions.InvalidTransactionException;
import com.skillstorm.transactionservice.exceptions.TransactionNotFoundException;
import com.skillstorm.transactionservice.models.Transaction;
import com.skillstorm.transactionservice.repositories.TransactionRepository;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    // Get a list of transactions for specific user using the userId
    public List<Transaction> getTransactionsByUserId(int userId) {
        Optional<List<Transaction>> transactionList = transactionRepository.findByUserId(userId);
        if (transactionList.isEmpty() || transactionList.get().isEmpty()) {
            throw new TransactionNotFoundException("Transactions for user ID " + userId + " not found");
        } else {
            return transactionList.get();
        }
    }

    // Get a list of transactions for specific user using the userId via RabbitMQ
    @RabbitListener(queues = "account-request")
    public void getTransactionsByUserIdRabbit(@Payload int userId,
            @Header(AmqpHeaders.CORRELATION_ID) String correlationId, @Header(AmqpHeaders.REPLY_TO) String replyQueue) {
        List<Transaction> transactionList = transactionRepository.findByUserId(userId)
                .orElseThrow(
                        () -> new TransactionNotFoundException("Transactions for user ID " + userId + " not found"));

        rabbitTemplate.convertAndSend(replyQueue, transactionList, message -> {
            message.getMessageProperties().setCorrelationId(correlationId);
            return message;
        });
    }

    /*
     * Get a list of transactions from a specific user using the userId, and the
     * list excludes the INCOME transaction category
     * This method will specifically be used by the Budget Service
     */
    @RabbitListener(queues = "budget-request")
    public void getTransactionsByUserIdExcludingIncome(@Payload int userId,
            @Header(AmqpHeaders.CORRELATION_ID) String correlationId, @Header(AmqpHeaders.REPLY_TO) String replyQueue) {
        // Look up all transactions for the User. Throw exception if user not found
        List<Transaction> transactionsList = transactionRepository.findByUserIdExcludeIncome(userId)
                .orElseThrow(
                        () -> new TransactionNotFoundException("Transactions for user ID " + userId + " not found"));

        // Send response back to the Budget-Service using the replyTo queue included in
        // the message header
        rabbitTemplate.convertAndSend(replyQueue, transactionsList, message -> {
            message.getMessageProperties().setCorrelationId(correlationId);
            return message;
        });
    }

    // Get a list of transactions of a specific account using the accountId
    public List<Transaction> getTransactionsByAccountId(int accountId) {
        Optional<List<Transaction>> transactionList = transactionRepository.findByAccountId(accountId);
        if (transactionList.isEmpty() || transactionList.get().isEmpty()) {
            throw new TransactionNotFoundException("Transactions for account ID " + accountId + " not found");
        } else {
            return transactionList.get();
        }
    }

    // Get a list of transactions by the vendor name and userId
    public List<Transaction> getTransactionsByUserIdAndVendorName(int userId, String vendorName) {
        Optional<List<Transaction>> transactionList = transactionRepository.findByUserIdAndVendorName(userId,
                vendorName);
        if (transactionList.isEmpty() || transactionList.get().isEmpty()) {
            throw new TransactionNotFoundException(
                    "Transactions for user ID " + userId + " and vendor " + vendorName + " not found");
        } else {
            return transactionList.get();
        }
    }

    // Get a list of the most recent 5 transactions of a specific user using userId
    public List<Transaction> getRecentFiveTransactions(int userId) {
        Optional<List<Transaction>> transactionList = transactionRepository.findRecentFiveTransaction(userId);
        if (transactionList.isEmpty() || transactionList.get().isEmpty()) {
            throw new TransactionNotFoundException("Unable to find most recent 5 transactions");
        } else {
            return transactionList.get();
        }
    }

    // get a list of transactions from the current Month of a specific user using
    // userId
    public List<Transaction> getTransactionsFromCurrentMonth(int userId) {
        LocalDate currentDate = LocalDate.now();
        int currentMonth = currentDate.getMonthValue();
        int currentYear = currentDate.getYear();

        Optional<List<Transaction>> transactionList = transactionRepository.findTransactionFromCurrentMonth(userId,
                currentMonth, currentYear);

        return transactionList.get();
    }

    // Create a transaction
    public Transaction createTransaction(int userId, Transaction transaction) {

        transaction.setUserId(userId);

        validateField(transaction.getAccountId() > 0, "Account ID is required");
        validateField(transaction.getUserId() > 0, "User ID is required");
        validateField(transaction.getVendorName() != null && !transaction.getVendorName().isEmpty(),
                "Vendor name is required");
        validateField(transaction.getAmount().compareTo(BigDecimal.ZERO) > 0, "Amount is required");
        validateField(transaction.getCategory() != null, "Category is required");
        validateField(transaction.getDate() != null, "Date is required");

        return transactionRepository.save(transaction);
    }

    // Update a transaction
    public Transaction updateTransaction(int transactionId, int userId, Transaction transaction) {
        Transaction existingTransaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new TransactionNotFoundException(
                        "Transaction with ID " + transaction.getTransactionId() + " not found"));

        if (existingTransaction.getUserId() != userId) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "You are not authorized to modify this transaction.");
        }

        if (transaction.getUserId() > 0) {
            existingTransaction.setUserId(transaction.getUserId());
        }

        if (transaction.getAccountId() > 0) {
            existingTransaction.setAccountId(transaction.getAccountId());
        }

        if (transaction.getVendorName() != "") {
            existingTransaction.setVendorName(transaction.getVendorName());
        }

        if (transaction.getAmount().compareTo(BigDecimal.ZERO) > 0) {
            existingTransaction.setAmount(transaction.getAmount());
        }

        if (transaction.getCategory() != null) {
            existingTransaction.setCategory(transaction.getCategory());
        }

        if (transaction.getDate() != null) {
            existingTransaction.setDate(transaction.getDate());
        }

        existingTransaction.setDescription(transaction.getDescription());

        return transactionRepository.save(existingTransaction);
    }

    // Delete a transaction
    public void deleteTransaction(int transactionId) {
        if (!transactionRepository.existsById(transactionId)) {
            throw new TransactionNotFoundException("Transaction with ID " + transactionId + " not found");
        }
        transactionRepository.deleteById(transactionId);
    }

    // delete transactions associated by a specific user using userId.
    public void deleteTransactionByUserId(int userId) {
        transactionRepository.deleteTransactionsByUserId(userId);
    }

    // helper method to validate Transaction fields and throw exception if invalid
    private void validateField(boolean condition, String errorMessage) {
        if (!condition) {
            throw new InvalidTransactionException(errorMessage);
        }
    }

    // validates the request by checking the userId within the headers. Throws
    // Unauthorized exception if UserId is not found or has invalid format
    public void validateRequestWithHeaders(HttpHeaders headers) {
        String headerUserIdStr = headers.getFirst("User-ID");
        if (headerUserIdStr == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User ID not found in request header");
        }

        try {
            Integer.parseInt(headerUserIdStr);
        } catch (NumberFormatException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid User ID format in request header");
        }
    }
}
