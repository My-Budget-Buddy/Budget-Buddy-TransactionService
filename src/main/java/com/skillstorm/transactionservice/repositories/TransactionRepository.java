package com.skillstorm.transactionservice.repositories;

import com.skillstorm.transactionservice.models.Transaction;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer>{


    //custom query to retrieve a list of transactions from a specific user using user id
    @Query("SELECT t FROM Transaction t WHERE t.userId = :userId")
    public Optional<List<Transaction>> findByUserId(@Param("userId") int userId);

    //custom query to retrieve a list of transactions from a specifc user using user id exluding the INCOME category of transactions
    @Query("SELECT t FROM Transaction t WHERE t.userId = :userId AND t.category != 'INCOME'")
    public Optional<List<Transaction>> findByUserIdExcludeIncome(@Param("userId") int userId);

    //custom query to retrieve a list of transactions from a specific account using the account id
    @Query("SELECT t FROM Transaction t WHERE t.accountId = :accountId")
    public Optional<List<Transaction>> findByAccountId(@Param("accountId") int accountId);

      // Custom query to retrieve a list of transactions for a specific user and vendor name
    @Query("SELECT t FROM Transaction t WHERE t.userId = :userId AND t.vendorName = :vendorName")
    public Optional<List<Transaction>> findByUserIdAndVendorName(@Param("userId") int userId, @Param("vendorName") String vendorName);

    //custom query to get the recent 5 transaction excluding the INCOME category of transactions
    @Query("SELECT t FROM Transaction t WHERE t.userId = :userId AND t.category != 'INCOME' ORDER BY t.date DESC LIMIT 5")
    public Optional<List<Transaction>> findRecentFiveTransaction(@Param("userId") int userId);

    //custom query to get the transaction for the current month excluding the INCOME category of transactions
    @Query("SELECT t FROM Transaction t WHERE t.userId = :userId AND t.category != 'INCOME' AND EXTRACT(MONTH from t.date) = :month AND EXTRACT(YEAR from t.date) = :year")
    public Optional<List<Transaction>> findTransactionFromCurrentMonth(@Param("userId") int userId, @Param("month") int month, @Param("year") int year);

    //custom query to delete transactions that are associated to a specific user using the userId
    @Transactional
    @Modifying
    @Query("DELETE FROM Transaction t WHERE t.userId = :userId")
    public void deleteTransactionsByUserId(@Param("userId") int userId);




}
