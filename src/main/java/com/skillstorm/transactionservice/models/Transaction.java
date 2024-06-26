package com.skillstorm.transactionservice.models;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

import jakarta.persistence.*;

@Entity
@Table(name = "transaction")
public class Transaction {
    
    // Primary key
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id")
    private int transactionId;

    @Column(name = "user_id")
    private int userId;

    @Column(name = "account_id")
    private int accountId;

    @Column(name = "vendor_name")
    private String vendorName;

    @Column(name = "transaction_amount")
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_category")
    private TransactionCategory category;

    @Column(name = "transaction_description")
    private String description;

    @Column(name = "transaction_date")
    private LocalDate date;

    public Transaction() {
    }

    public Transaction(int userId, int accountId, String vendorName, BigDecimal amount, TransactionCategory category, String description, LocalDate date) {
        this.userId = userId;
        this.accountId = accountId;
        this.vendorName = vendorName;
        this.amount = amount;
        this.category = category;
        this.description = description;
        this.date = date;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public TransactionCategory getCategory() {
        return category;
    }

    public void setCategory(TransactionCategory category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return transactionId == that.transactionId && userId == that.userId && accountId == that.accountId && Objects.equals(vendorName, that.vendorName) && Objects.equals(amount, that.amount) && category == that.category && Objects.equals(description, that.description) && Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transactionId, userId, accountId, vendorName, amount, category, description, date);
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "transactionId=" + transactionId +
                ", userId=" + userId +
                ", accountId=" + accountId +
                ", vendorName='" + vendorName + '\'' +
                ", amount=" + amount +
                ", category='" + category.toString() + '\'' +
                ", description='" + description + '\'' +
                ", date=" + date +
                '}';
    }
}
