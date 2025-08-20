package com.modernized.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * Account balances tracked by specific transaction categories for interest calculation and reporting
 * Source: tcatbal.txt reference data structure
 */
@Entity
@Table(name = "transaction_category_balance")
public class TransactionCategoryBalance {

    @EmbeddedId
    @NotNull
    private TransactionCategoryBalanceId id;

    @NotNull
    @Column(name = "tran_cat_bal", precision = 11, scale = 2)
    private BigDecimal tranCatBal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trancat_acct_id", referencedColumnName = "acct_id", insertable = false, updatable = false)
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
        @JoinColumn(name = "trancat_type_cd", referencedColumnName = "tran_type_cd", insertable = false, updatable = false),
        @JoinColumn(name = "trancat_cd", referencedColumnName = "tran_cat_cd", insertable = false, updatable = false)
    })
    private TransactionCategory transactionCategory;

    public TransactionCategoryBalance() {}

    public TransactionCategoryBalance(TransactionCategoryBalanceId id, BigDecimal tranCatBal) {
        this.id = id;
        this.tranCatBal = tranCatBal;
    }

    public TransactionCategoryBalanceId getId() { return id; }
    public void setId(TransactionCategoryBalanceId id) { this.id = id; }

    public BigDecimal getTranCatBal() { return tranCatBal; }
    public void setTranCatBal(BigDecimal tranCatBal) { this.tranCatBal = tranCatBal; }

    public Account getAccount() { return account; }
    public void setAccount(Account account) { this.account = account; }

    public TransactionCategory getTransactionCategory() { return transactionCategory; }
    public void setTransactionCategory(TransactionCategory transactionCategory) { this.transactionCategory = transactionCategory; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransactionCategoryBalance that = (TransactionCategoryBalance) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "TransactionCategoryBalance{" +
                "id=" + id +
                ", tranCatBal=" + tranCatBal +
                '}';
    }
}
