package com.modernized.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * Daily processed transactions for settlement and batch processing operations
 * Source: CVTRA02Y.cpy, lines 4-18
 */
@Entity
@Table(name = "daily_transaction")
public class DailyTransaction {

    @Id
    @Column(name = "dalytran_id", length = 16)
    private String dalytranId;

    @Column(name = "dalytran_type_cd", length = 2)
    private String dalytranTypeCd;

    @Column(name = "dalytran_cat_cd")
    private Integer dalytranCatCd;

    @Column(name = "dalytran_source", length = 10)
    private String dalytranSource;

    @Column(name = "dalytran_desc", length = 100)
    private String dalytranDesc;

    @Column(name = "dalytran_amt", precision = 11, scale = 2)
    private BigDecimal dalytranAmt;

    @Column(name = "dalytran_merchant_id")
    private Long dalytranMerchantId;

    @Column(name = "dalytran_merchant_name", length = 50)
    private String dalytranMerchantName;

    @Column(name = "dalytran_merchant_city", length = 50)
    private String dalytranMerchantCity;

    @Column(name = "dalytran_merchant_zip", length = 10)
    private String dalytranMerchantZip;

    @Column(name = "dalytran_card_num", length = 16)
    private String dalytranCardNum;

    @Column(name = "dalytran_orig_ts", length = 26)
    private String dalytranOrigTs;

    @Column(name = "dalytran_proc_ts", length = 26)
    private String dalytranProcTs;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dalytran_card_num", referencedColumnName = "card_num", insertable = false, updatable = false)
    private Card card;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dalytran_id", referencedColumnName = "tran_id", insertable = false, updatable = false)
    private Transaction transaction;

    public DailyTransaction() {}

    public DailyTransaction(String dalytranId, String dalytranTypeCd, Integer dalytranCatCd,
                           String dalytranSource, String dalytranDesc, BigDecimal dalytranAmt,
                           Long dalytranMerchantId, String dalytranMerchantName,
                           String dalytranMerchantCity, String dalytranMerchantZip,
                           String dalytranCardNum, String dalytranOrigTs, String dalytranProcTs) {
        this.dalytranId = dalytranId;
        this.dalytranTypeCd = dalytranTypeCd;
        this.dalytranCatCd = dalytranCatCd;
        this.dalytranSource = dalytranSource;
        this.dalytranDesc = dalytranDesc;
        this.dalytranAmt = dalytranAmt;
        this.dalytranMerchantId = dalytranMerchantId;
        this.dalytranMerchantName = dalytranMerchantName;
        this.dalytranMerchantCity = dalytranMerchantCity;
        this.dalytranMerchantZip = dalytranMerchantZip;
        this.dalytranCardNum = dalytranCardNum;
        this.dalytranOrigTs = dalytranOrigTs;
        this.dalytranProcTs = dalytranProcTs;
    }

    public String getDalytranId() { return dalytranId; }
    public void setDalytranId(String dalytranId) { this.dalytranId = dalytranId; }

    public String getDalytranTypeCd() { return dalytranTypeCd; }
    public void setDalytranTypeCd(String dalytranTypeCd) { this.dalytranTypeCd = dalytranTypeCd; }

    public Integer getDalytranCatCd() { return dalytranCatCd; }
    public void setDalytranCatCd(Integer dalytranCatCd) { this.dalytranCatCd = dalytranCatCd; }

    public String getDalytranSource() { return dalytranSource; }
    public void setDalytranSource(String dalytranSource) { this.dalytranSource = dalytranSource; }

    public String getDalytranDesc() { return dalytranDesc; }
    public void setDalytranDesc(String dalytranDesc) { this.dalytranDesc = dalytranDesc; }

    public BigDecimal getDalytranAmt() { return dalytranAmt; }
    public void setDalytranAmt(BigDecimal dalytranAmt) { this.dalytranAmt = dalytranAmt; }

    public Long getDalytranMerchantId() { return dalytranMerchantId; }
    public void setDalytranMerchantId(Long dalytranMerchantId) { this.dalytranMerchantId = dalytranMerchantId; }

    public String getDalytranMerchantName() { return dalytranMerchantName; }
    public void setDalytranMerchantName(String dalytranMerchantName) { this.dalytranMerchantName = dalytranMerchantName; }

    public String getDalytranMerchantCity() { return dalytranMerchantCity; }
    public void setDalytranMerchantCity(String dalytranMerchantCity) { this.dalytranMerchantCity = dalytranMerchantCity; }

    public String getDalytranMerchantZip() { return dalytranMerchantZip; }
    public void setDalytranMerchantZip(String dalytranMerchantZip) { this.dalytranMerchantZip = dalytranMerchantZip; }

    public String getDalytranCardNum() { return dalytranCardNum; }
    public void setDalytranCardNum(String dalytranCardNum) { this.dalytranCardNum = dalytranCardNum; }

    public String getDalytranOrigTs() { return dalytranOrigTs; }
    public void setDalytranOrigTs(String dalytranOrigTs) { this.dalytranOrigTs = dalytranOrigTs; }

    public String getDalytranProcTs() { return dalytranProcTs; }
    public void setDalytranProcTs(String dalytranProcTs) { this.dalytranProcTs = dalytranProcTs; }

    public Card getCard() { return card; }
    public void setCard(Card card) { this.card = card; }

    public Transaction getTransaction() { return transaction; }
    public void setTransaction(Transaction transaction) { this.transaction = transaction; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DailyTransaction that = (DailyTransaction) o;
        return Objects.equals(dalytranId, that.dalytranId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dalytranId);
    }

    @Override
    public String toString() {
        return "DailyTransaction{" +
                "dalytranId='" + dalytranId + '\'' +
                ", dalytranTypeCd='" + dalytranTypeCd + '\'' +
                ", dalytranAmt=" + dalytranAmt +
                '}';
    }
}
