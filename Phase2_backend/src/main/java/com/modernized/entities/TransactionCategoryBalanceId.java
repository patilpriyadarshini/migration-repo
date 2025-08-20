package com.modernized.entities;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Column;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class TransactionCategoryBalanceId implements Serializable {

    @Column(name = "trancat_acct_id")
    private Long trancatAcctId;
    
    @Column(name = "trancat_type_cd")
    private String trancatTypeCd;
    
    @Column(name = "trancat_cd")
    private Integer trancatCd;

    public TransactionCategoryBalanceId() {}

    public TransactionCategoryBalanceId(Long trancatAcctId, String trancatTypeCd, Integer trancatCd) {
        this.trancatAcctId = trancatAcctId;
        this.trancatTypeCd = trancatTypeCd;
        this.trancatCd = trancatCd;
    }

    public Long getTrancatAcctId() { return trancatAcctId; }
    public void setTrancatAcctId(Long trancatAcctId) { this.trancatAcctId = trancatAcctId; }

    public String getTrancatTypeCd() { return trancatTypeCd; }
    public void setTrancatTypeCd(String trancatTypeCd) { this.trancatTypeCd = trancatTypeCd; }

    public Integer getTrancatCd() { return trancatCd; }
    public void setTrancatCd(Integer trancatCd) { this.trancatCd = trancatCd; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransactionCategoryBalanceId that = (TransactionCategoryBalanceId) o;
        return Objects.equals(trancatAcctId, that.trancatAcctId) &&
               Objects.equals(trancatTypeCd, that.trancatTypeCd) &&
               Objects.equals(trancatCd, that.trancatCd);
    }

    @Override
    public int hashCode() {
        return Objects.hash(trancatAcctId, trancatTypeCd, trancatCd);
    }
}
