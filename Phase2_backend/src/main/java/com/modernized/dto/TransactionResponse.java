package com.modernized.dto;

import java.math.BigDecimal;

public class TransactionResponse {
    private String tranId;
    private String cardNum;
    private String tranTypeCd;
    private String tranCatCd;
    private String tranSource;
    private String tranDesc;
    private BigDecimal tranAmt;
    private String origTs;
    private String procTs;
    private String merchantId;
    private String merchantName;
    private String merchantCity;
    private String merchantZip;

    public TransactionResponse() {}

    public String getTranId() { return tranId; }
    public void setTranId(String tranId) { this.tranId = tranId; }

    public String getCardNum() { return cardNum; }
    public void setCardNum(String cardNum) { this.cardNum = cardNum; }

    public String getTranTypeCd() { return tranTypeCd; }
    public void setTranTypeCd(String tranTypeCd) { this.tranTypeCd = tranTypeCd; }

    public String getTranCatCd() { return tranCatCd; }
    public void setTranCatCd(String tranCatCd) { this.tranCatCd = tranCatCd; }

    public String getTranSource() { return tranSource; }
    public void setTranSource(String tranSource) { this.tranSource = tranSource; }

    public String getTranDesc() { return tranDesc; }
    public void setTranDesc(String tranDesc) { this.tranDesc = tranDesc; }

    public BigDecimal getTranAmt() { return tranAmt; }
    public void setTranAmt(BigDecimal tranAmt) { this.tranAmt = tranAmt; }

    public String getOrigTs() { return origTs; }
    public void setOrigTs(String origTs) { this.origTs = origTs; }

    public String getProcTs() { return procTs; }
    public void setProcTs(String procTs) { this.procTs = procTs; }

    public String getMerchantId() { return merchantId; }
    public void setMerchantId(String merchantId) { this.merchantId = merchantId; }

    public String getMerchantName() { return merchantName; }
    public void setMerchantName(String merchantName) { this.merchantName = merchantName; }

    public String getMerchantCity() { return merchantCity; }
    public void setMerchantCity(String merchantCity) { this.merchantCity = merchantCity; }

    public String getMerchantZip() { return merchantZip; }
    public void setMerchantZip(String merchantZip) { this.merchantZip = merchantZip; }
}
