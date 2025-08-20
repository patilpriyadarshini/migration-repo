package com.modernized.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public class BillPaymentRequest {
    
    @NotNull(message = "Account ID cannot be null")
    private Long accountId;

    @NotBlank(message = "Confirmation is required")
    @Pattern(regexp = "[YN]", message = "Confirmation must be Y or N")
    private String confirmation;

    @NotNull(message = "Payment amount cannot be null")
    @DecimalMin(value = "0.01", message = "Payment amount must be greater than zero")
    private BigDecimal paymentAmount;

    @NotBlank(message = "Payment date is required")
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "Payment date must be in YYYY-MM-DD format")
    private String paymentDate;

    public BillPaymentRequest() {}

    public BillPaymentRequest(Long accountId, String confirmation, BigDecimal paymentAmount, String paymentDate) {
        this.accountId = accountId;
        this.confirmation = confirmation;
        this.paymentAmount = paymentAmount;
        this.paymentDate = paymentDate;
    }

    public Long getAccountId() { return accountId; }
    public void setAccountId(Long accountId) { this.accountId = accountId; }

    public String getConfirmation() { return confirmation; }
    public void setConfirmation(String confirmation) { this.confirmation = confirmation; }

    public BigDecimal getPaymentAmount() { return paymentAmount; }
    public void setPaymentAmount(BigDecimal paymentAmount) { this.paymentAmount = paymentAmount; }

    public String getPaymentDate() { return paymentDate; }
    public void setPaymentDate(String paymentDate) { this.paymentDate = paymentDate; }
}
