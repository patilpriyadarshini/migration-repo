package com.modernized.dto;

import java.math.BigDecimal;

public class BillPaymentResponse {
    private Long accountId;
    private BigDecimal currentBalance;
    private BigDecimal paymentAmount;
    private String message;
    private boolean success;

    public BillPaymentResponse() {}

    public BillPaymentResponse(Long accountId, BigDecimal currentBalance, BigDecimal paymentAmount, 
                              boolean success, String message) {
        this.accountId = accountId;
        this.currentBalance = currentBalance;
        this.paymentAmount = paymentAmount;
        this.success = success;
        this.message = message;
    }

    public Long getAccountId() { return accountId; }
    public void setAccountId(Long accountId) { this.accountId = accountId; }

    public BigDecimal getCurrentBalance() { return currentBalance; }
    public void setCurrentBalance(BigDecimal currentBalance) { this.currentBalance = currentBalance; }

    public BigDecimal getPaymentAmount() { return paymentAmount; }
    public void setPaymentAmount(BigDecimal paymentAmount) { this.paymentAmount = paymentAmount; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
}
