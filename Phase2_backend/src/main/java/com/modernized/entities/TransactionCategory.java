package com.modernized.entities;

import jakarta.persistence.*;
import java.util.List;
import java.util.Objects;

/**
 * Detailed business classification for specific transaction subcategories within types
 * Source: trancatg.txt reference data, lines 1-18
 */
@Entity
@Table(name = "transaction_category")
public class TransactionCategory {

    @Id
    @Column(name = "tran_cat_cd")
    private Integer tranCatCd;

    @Column(name = "tran_type_cd", length = 2)
    private String tranTypeCd;

    @Column(name = "tran_cat_type_desc", length = 50)
    private String tranCatTypeDesc;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tran_type_cd", referencedColumnName = "tran_type", insertable = false, updatable = false)
    private TransactionType transactionType;

    @OneToMany(mappedBy = "transactionCategory", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Transaction> transactions;

    @OneToMany(mappedBy = "transactionCategory", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TransactionCategoryBalance> transactionCategoryBalances;

    @OneToMany(mappedBy = "transactionCategory", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DisclosureGroup> disclosureGroups;

    public TransactionCategory() {}

    public TransactionCategory(Integer tranCatCd, String tranTypeCd, String tranCatTypeDesc) {
        this.tranCatCd = tranCatCd;
        this.tranTypeCd = tranTypeCd;
        this.tranCatTypeDesc = tranCatTypeDesc;
    }

    public Integer getTranCatCd() { return tranCatCd; }
    public void setTranCatCd(Integer tranCatCd) { this.tranCatCd = tranCatCd; }

    public String getTranTypeCd() { return tranTypeCd; }
    public void setTranTypeCd(String tranTypeCd) { this.tranTypeCd = tranTypeCd; }

    public String getTranCatTypeDesc() { return tranCatTypeDesc; }
    public void setTranCatTypeDesc(String tranCatTypeDesc) { this.tranCatTypeDesc = tranCatTypeDesc; }

    public TransactionType getTransactionType() { return transactionType; }
    public void setTransactionType(TransactionType transactionType) { this.transactionType = transactionType; }

    public List<Transaction> getTransactions() { return transactions; }
    public void setTransactions(List<Transaction> transactions) { this.transactions = transactions; }

    public List<TransactionCategoryBalance> getTransactionCategoryBalances() { return transactionCategoryBalances; }
    public void setTransactionCategoryBalances(List<TransactionCategoryBalance> transactionCategoryBalances) { this.transactionCategoryBalances = transactionCategoryBalances; }

    public List<DisclosureGroup> getDisclosureGroups() { return disclosureGroups; }
    public void setDisclosureGroups(List<DisclosureGroup> disclosureGroups) { this.disclosureGroups = disclosureGroups; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransactionCategory that = (TransactionCategory) o;
        return Objects.equals(tranCatCd, that.tranCatCd);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tranCatCd);
    }

    @Override
    public String toString() {
        return "TransactionCategory{" +
                "tranCatCd=" + tranCatCd +
                ", tranTypeCd='" + tranTypeCd + '\'' +
                ", tranCatTypeDesc='" + tranCatTypeDesc + '\'' +
                '}';
    }
}
