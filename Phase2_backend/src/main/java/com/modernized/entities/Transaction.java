package com.modernized.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Financial events representing purchases, payments, credits, and other account activities
 * Source: CVTRA05Y.cpy, lines 4-18
 */
@Entity
@Table(name = "transaction")
public class Transaction {

    @Id
    @NotNull
    @Size(min = 1, max = 16)
    @Column(name = "tran_id", length = 16)
    private String tranId;

    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "tran_type_cd", length = 2)
    private String tranTypeCd;

    @NotNull
    @Column(name = "tran_cat_cd")
    private Integer tranCatCd;

    @Size(max = 10)
    @Column(name = "tran_source", length = 10)
    private String tranSource;

    @Size(max = 100)
    @Column(name = "tran_desc", length = 100)
    private String tranDesc;

    @NotNull
    @Column(name = "tran_amt", precision = 11, scale = 2)
    private BigDecimal tranAmt;

    @Column(name = "tran_merchant_id")
    private Long tranMerchantId;

    @Size(max = 50)
    @Column(name = "tran_merchant_name", length = 50)
    private String tranMerchantName;

    @Size(max = 50)
    @Column(name = "tran_merchant_city", length = 50)
    private String tranMerchantCity;

    @Size(max = 10)
    @Column(name = "tran_merchant_zip", length = 10)
    private String tranMerchantZip;

    @NotNull
    @Size(min = 1, max = 16)
    @Column(name = "tran_card_num", length = 16)
    private String tranCardNum;

    @Column(name = "tran_orig_ts", length = 26)
    private String tranOrigTs;

    @Column(name = "tran_proc_ts", length = 26)
    private String tranProcTs;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tran_card_num", referencedColumnName = "card_num", insertable = false, updatable = false)
    private Card card;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tran_type_cd", referencedColumnName = "tran_type", insertable = false, updatable = false)
    private TransactionType transactionType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tran_cat_cd", referencedColumnName = "tran_cat_cd", insertable = false, updatable = false)
    private TransactionCategory transactionCategory;

    @OneToOne(mappedBy = "transaction", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private DailyTransaction dailyTransaction;

    public Transaction() {}

    public Transaction(String tranId, String tranTypeCd, Integer tranCatCd, String tranSource,
                      String tranDesc, BigDecimal tranAmt, Long tranMerchantId,
                      String tranMerchantName, String tranMerchantCity, String tranMerchantZip,
                      String tranCardNum, String tranOrigTs, String tranProcTs) {
        this.tranId = tranId;
        this.tranTypeCd = tranTypeCd;
        this.tranCatCd = tranCatCd;
        this.tranSource = tranSource;
        this.tranDesc = tranDesc;
        this.tranAmt = tranAmt;
        this.tranMerchantId = tranMerchantId;
        this.tranMerchantName = tranMerchantName;
        this.tranMerchantCity = tranMerchantCity;
        this.tranMerchantZip = tranMerchantZip;
        this.tranCardNum = tranCardNum;
        this.tranOrigTs = tranOrigTs;
        this.tranProcTs = tranProcTs;
    }

    public String getTranId() { return tranId; }
    public void setTranId(String tranId) { this.tranId = tranId; }

    public String getTranTypeCd() { return tranTypeCd; }
    public void setTranTypeCd(String tranTypeCd) { this.tranTypeCd = tranTypeCd; }

    public Integer getTranCatCd() { return tranCatCd; }
    public void setTranCatCd(Integer tranCatCd) { this.tranCatCd = tranCatCd; }

    public String getTranSource() { return tranSource; }
    public void setTranSource(String tranSource) { this.tranSource = tranSource; }

    public String getTranDesc() { return tranDesc; }
    public void setTranDesc(String tranDesc) { this.tranDesc = tranDesc; }

    public BigDecimal getTranAmt() { return tranAmt; }
    public void setTranAmt(BigDecimal tranAmt) { this.tranAmt = tranAmt; }

    public Long getTranMerchantId() { return tranMerchantId; }
    public void setTranMerchantId(Long tranMerchantId) { this.tranMerchantId = tranMerchantId; }

    public String getTranMerchantName() { return tranMerchantName; }
    public void setTranMerchantName(String tranMerchantName) { this.tranMerchantName = tranMerchantName; }

    public String getTranMerchantCity() { return tranMerchantCity; }
    public void setTranMerchantCity(String tranMerchantCity) { this.tranMerchantCity = tranMerchantCity; }

    public String getTranMerchantZip() { return tranMerchantZip; }
    public void setTranMerchantZip(String tranMerchantZip) { this.tranMerchantZip = tranMerchantZip; }

    public String getTranCardNum() { return tranCardNum; }
    public void setTranCardNum(String tranCardNum) { this.tranCardNum = tranCardNum; }

    public String getTranOrigTs() { return tranOrigTs; }
    public void setTranOrigTs(String tranOrigTs) { this.tranOrigTs = tranOrigTs; }

    public String getTranProcTs() { return tranProcTs; }
    public void setTranProcTs(String tranProcTs) { this.tranProcTs = tranProcTs; }

    public Card getCard() { return card; }
    public void setCard(Card card) { this.card = card; }

    public TransactionType getTransactionType() { return transactionType; }
    public void setTransactionType(TransactionType transactionType) { this.transactionType = transactionType; }

    public TransactionCategory getTransactionCategory() { return transactionCategory; }
    public void setTransactionCategory(TransactionCategory transactionCategory) { this.transactionCategory = transactionCategory; }

    public DailyTransaction getDailyTransaction() { return dailyTransaction; }
    public void setDailyTransaction(DailyTransaction dailyTransaction) { this.dailyTransaction = dailyTransaction; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return Objects.equals(tranId, that.tranId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tranId);
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "tranId='" + tranId + '\'' +
                ", tranTypeCd='" + tranTypeCd + '\'' +
                ", tranAmt=" + tranAmt +
                '}';
    }
}
