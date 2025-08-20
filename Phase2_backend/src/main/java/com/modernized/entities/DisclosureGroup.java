package com.modernized.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

/**
 * Business rules and rate structures for different transaction types and categories
 * Source: discgrp.txt reference data structure
 */
@Entity
@Table(name = "disclosure_group")
public class DisclosureGroup {

    @EmbeddedId
    private DisclosureGroupId id;

    @Column(name = "dis_int_rate", precision = 6, scale = 2)
    private BigDecimal disIntRate;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dis_tran_type_cd", referencedColumnName = "tran_type", insertable = false, updatable = false)
    private TransactionType transactionType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dis_tran_cat_cd", referencedColumnName = "tran_cat_cd", insertable = false, updatable = false)
    private TransactionCategory transactionCategory;

    public DisclosureGroup() {}

    public DisclosureGroup(DisclosureGroupId id, BigDecimal disIntRate) {
        this.id = id;
        this.disIntRate = disIntRate;
    }

    public DisclosureGroupId getId() { return id; }
    public void setId(DisclosureGroupId id) { this.id = id; }

    public BigDecimal getDisIntRate() { return disIntRate; }
    public void setDisIntRate(BigDecimal disIntRate) { this.disIntRate = disIntRate; }


    public TransactionType getTransactionType() { return transactionType; }
    public void setTransactionType(TransactionType transactionType) { this.transactionType = transactionType; }

    public TransactionCategory getTransactionCategory() { return transactionCategory; }
    public void setTransactionCategory(TransactionCategory transactionCategory) { this.transactionCategory = transactionCategory; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DisclosureGroup that = (DisclosureGroup) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "DisclosureGroup{" +
                "id=" + id +
                ", disIntRate=" + disIntRate +
                '}';
    }
}
