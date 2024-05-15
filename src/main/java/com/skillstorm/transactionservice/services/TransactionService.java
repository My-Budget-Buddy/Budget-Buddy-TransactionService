package com.skillstorm.transactionservice.services;

import com.skillstorm.transactionservice.models.Transaction;
import com.skillstorm.transactionservice.repositories.TransactionRepository;
import org.hibernate.internal.TransactionManagement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    //get all transactions
//    public List<Transaction> getallTransactions() {
//        return transactionRepository.findAll();
//    }

    //get a list of transactions by userid
    public List<Transaction> getTransactionByUserId(int userId){
        Optional<List<Transaction>> transactionOptionalList = transactionRepository.findByUserId(userId);

        if(transactionOptionalList.isPresent()){
            return transactionOptionalList.get();
        }
        else {
            return null;
        }
    }

    //get a transaction by transactionid
//    public Transaction getTransactionById(int transactionId){
//        Optional<Transaction> transactionOptional = transactionRepository.findById(transactionId);
//
//        if(transactionOptional.isPresent()){
//            return transactionOptional.get();
//        } else {
//            return null;
//        }
//    }

    //get a transaction by accountid
//    public Transaction getTransactionByAccountId(int accountId){
//        Optional<List<Transaction>> transactionOptional = transactionRepository.findByAccountId(accountId);
//
//        if(transactionOptional.isPresent()){
//            return transactionOptional.get();
//        } else {
//            return null;
//        }
//    }


    //get a transaction by vendor name
//    public List<Transaction> getTransactionByVendorName(String vendorName){
//        Optional<List<Transaction>> transactionOptional = transactionRepository.findByVendorName(vendorName);
//
//        if(transactionOptional.isPresent()){
//            return transactionOptional.get();
//        } else {
//            return null;
//        }
//    }



    //get a transaction by date
//    public List<Transaction> getTransactionFromLastBy7Days(){
//        LocalDate date = LocalDate.now().minusDays(7);
//        Optional<List<Transaction>> transactionOptionalList = transactionRepository.findFromLast7Days(date);
//
//        if(transactionOptionalList.isPresent()){
//            return transactionOptionalList.get();
//        } else {
//            return null;
//        }
//    }

    //get a transaction by category
//    public Transaction getTransactionByCategory(String category){
//        Optional<Transaction> transactionOptional = transactionRepository.findByCategory(category);
//
//        if(transactionOptional.isPresent()){
//            return transactionOptional.get();
//        } else {
//            return null;
//        }
//    }

    //create a transaction
    public Transaction createTransaction(Transaction transaction){
        return transactionRepository.save(transaction);
    }

    //update a transaction
    public Transaction updateTransaction(Transaction transaction){
        int transactionId = transaction.getTransactionId();

        Optional<Transaction> existingTransaction = transactionRepository.findById(transactionId);


        if(existingTransaction.isPresent()){


            Transaction updatedTransaction = existingTransaction.get();

            if(transaction.getUserId() != 0) {
                updatedTransaction.setUserId(transaction.getUserId());
            }

            if(transaction.getAccountId() != 0) {
                updatedTransaction.setAccountId(transaction.getAccountId());
            }

            if(transaction.getVendorName() != null) {
                updatedTransaction.setVendorName(transaction.getVendorName());
            }

            if(transaction.getAmount() != 0) {
                updatedTransaction.setAmount(transaction.getAmount());
            }

            if(transaction.getCategory() != null) {
                updatedTransaction.setCategory(transaction.getCategory());
            }

            if(transaction.getDescription() != null) {
                updatedTransaction.setDescription(transaction.getDescription());
            }

            if(transaction.getDate() != null) {
                updatedTransaction.setDate(transaction.getDate());
            }

            return transactionRepository.save(updatedTransaction);
        } else {
            return null;
        }

    }

    //delete a transaction
    public void deleteTransaction(int transactionId){
        transactionRepository.deleteById(transactionId);
    }
}
