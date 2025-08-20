package com.modernized.entities;

import jakarta.persistence.*;
import java.util.List;
import java.util.Objects;

/**
 * Business classification scheme for categorizing different types of financial transactions
 * Source: trantype.txt reference data, lines 1-7
 */
@Entity
@Table(name = "transaction_type")
public class TransactionType {

    @Id
    @Column(name = "tran_type", length = 2)
    private String tranType;

    @Column(name = "tran_type_desc", length = 50)
    private String tranTypeDesc;

    @OneToMany(mappedBy = "transactionType", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Transaction> transactions;

    @OneToMany(mappedBy = "transactionType", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TransactionCategory> transactionCategories;

    @OneToMany(mappedBy = "transactionType", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DisclosureGroup> disclosureGroups;

    public TransactionType() {}

    public TransactionType(String tranType, String tranTypeDesc) {
        this.tranType = tranType;
        this.tranTypeDesc = tranTypeDesc;
    }

    public String getTranType() { return tranType; }
    public void setTranType(String tranType) { this.tranType = tranType; }

    public String getTranTypeDesc() { return tranTypeDesc; }
    public void setTranTypeDesc(String tranTypeDesc) { this.tranTypeDesc = tranTypeDesc; }

    public List<Transaction> getTransactions() { return transactions; }
    public void setTransactions(List<Transaction> transactions) { this.transactions = transactions; }

    public List<TransactionCategory> getTransactionCategories() { return transactionCategories; }
    public void setTransactionCategories(List<TransactionCategory> transactionCategories) { this.transactionCategories = transactionCategories; }

    public List<DisclosureGroup> getDisclosureGroups() { return disclosureGroups; }
    public void setDisclosureGroups(List<DisclosureGroup> disclosureGroups) { this.disclosureGroups = disclosureGroups; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransactionType that = (TransactionType) o;
        return Objects.equals(tranType, that.tranType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tranType);
    }

    @Override
    public String toString() {
        return "TransactionType{" +
                "tranType='" + tranType + '\'' +
                ", tranTypeDesc='" + tranTypeDesc + '\'' +
                '}';
    }
}
