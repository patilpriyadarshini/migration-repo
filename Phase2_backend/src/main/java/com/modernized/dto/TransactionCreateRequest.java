package com.modernized.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public class TransactionCreateRequest {
    
    private String acctId;

    @Pattern(regexp = "^$|\\d{16}", message = "Card number must be 16 digits or empty")
    private String cardNum;

    @NotBlank(message = "Transaction type code cannot be empty")
    @Pattern(regexp = "\\d+", message = "Transaction type code must be numeric")
    private String tranTypeCd;

    @NotBlank(message = "Transaction category code cannot be empty")
    @Pattern(regexp = "\\d+", message = "Transaction category code must be numeric")
    private String tranCatCd;

    private String tranSource;

    private String tranDesc;

    @NotNull(message = "Transaction amount cannot be null")
    private BigDecimal tranAmt;

    @NotBlank(message = "Origin date cannot be empty")
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "Origin date must be in YYYY-MM-DD format")
    private String origDate;

    @NotBlank(message = "Process date cannot be empty")
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "Process date must be in YYYY-MM-DD format")
    private String procDate;

    private String merchantId;

    @NotBlank(message = "Merchant name cannot be empty")
    private String merchantName;

    @NotBlank(message = "Merchant city cannot be empty")
    private String merchantCity;

    @NotBlank(message = "Merchant zip cannot be empty")
    private String merchantZip;

    @NotBlank(message = "Confirmation is required")
    @Pattern(regexp = "[YN]", message = "Confirmation must be Y or N")
    private String confirmation;

    public TransactionCreateRequest() {}

    public String getAcctId() { return acctId; }
    public void setAcctId(String acctId) { this.acctId = acctId; }

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

    public String getOrigDate() { return origDate; }
    public void setOrigDate(String origDate) { this.origDate = origDate; }

    public String getProcDate() { return procDate; }
    public void setProcDate(String procDate) { this.procDate = procDate; }

    public String getMerchantId() { return merchantId; }
    public void setMerchantId(String merchantId) { this.merchantId = merchantId; }

    public String getMerchantName() { return merchantName; }
    public void setMerchantName(String merchantName) { this.merchantName = merchantName; }

    public String getMerchantCity() { return merchantCity; }
    public void setMerchantCity(String merchantCity) { this.merchantCity = merchantCity; }

    public String getMerchantZip() { return merchantZip; }
    public void setMerchantZip(String merchantZip) { this.merchantZip = merchantZip; }

    public String getConfirmation() { return confirmation; }
    public void setConfirmation(String confirmation) { this.confirmation = confirmation; }
}
