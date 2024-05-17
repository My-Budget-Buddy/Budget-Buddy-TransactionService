package com.skillstorm.transactionservice.repositories;

import com.skillstorm.transactionservice.models.Transaction;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Integer>{


    //update a transaction
//    @Transactional
//    @Modifying
//    @Query("UPDATE Transaction t SET t.amount = :transactionAmount, t.date = :transactionDate, " +
//        "t.category = :transactionType, t.accountId = :accountId, t.userId = :userId, t.vendorName = :transactionVendorName,  " +
//            " t.description = :transactionDescription WHERE t.transactionId = :transactionId")
//    int updateTransaction(@Param("transactionId") int transactionId, @Param("amount") double transactionAmount,
//                          @Param("date") String transactionDate, @Param("category") String transactionType,
//                          @Param("accountId") int accountId, @Param("userId") int userId,
//                          @Param("vendorName") String transactionVendorName,
//                          @Param("description") String transactionDescription);

    @Query("SELECT t FROM Transaction t WHERE t.userId = :userId")
    public Optional<List<Transaction>> findByUserId(@Param("userId") int userId);

    @Query("SELECT t FROM Transaction t WHERE t.userId = :userId AND t.category != 'INCOME'")
    public Optional<List<Transaction>> findByUserIdExcludeIncome(@Param("userId") int userId);

    @Query("SELECT t FROM Transaction t WHERE t.accountId = :accountId")
    public Optional<List<Transaction>> findByAccountId(@Param("accountId") int accountId);

    @Query("SELECT t FROM Transaction t WHERE t.vendorName = :vendorName")
    public Optional<List<Transaction>> findByVendorName(@Param("vendorName") String vendorName);

    @Query("SELECT t FROM Transaction t WHERE t.category = :category")
    public Optional<List<Transaction>> findByCategory(@Param("category") String category);

    @Query("SELECT t FROM Transaction t WHERE t.date >= :startDate")
    public Optional<List<Transaction>> findFromLast7Days(@Param("startDate") LocalDate startDate);




}
