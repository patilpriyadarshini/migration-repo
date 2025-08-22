package com.modernized.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public class AccountUpdateRequest {
    
    @NotBlank(message = "Account active status cannot be empty")
    @Pattern(regexp = "[YN]", message = "Account status must be Y or N")
    private String acctActiveStatus;

    @NotNull(message = "Current balance cannot be null")
    @DecimalMin(value = "0.0", inclusive = true, message = "Current balance must be non-negative")
    private BigDecimal acctCurrBal;

    @NotNull(message = "Credit limit cannot be null")
    @DecimalMin(value = "0.01", message = "Credit limit must be greater than zero")
    private BigDecimal acctCreditLimit;

    @NotNull(message = "Cash credit limit cannot be null")
    @DecimalMin(value = "0.0", inclusive = true, message = "Cash credit limit must be non-negative")
    private BigDecimal acctCashCreditLimit;

    @NotBlank(message = "Account open date cannot be empty")
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "Date must be in YYYY-MM-DD format")
    private String acctOpenDate;

    @NotBlank(message = "Account expiration date cannot be empty")
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "Date must be in YYYY-MM-DD format")
    private String acctExpirationDate;

    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "Date must be in YYYY-MM-DD format")
    private String acctReissueDate;

    @NotNull(message = "Current cycle credit cannot be null")
    @DecimalMin(value = "0.0", inclusive = true, message = "Current cycle credit must be non-negative")
    private BigDecimal acctCurrCycCredit;

    @NotNull(message = "Current cycle debit cannot be null")
    @DecimalMin(value = "0.0", inclusive = true, message = "Current cycle debit must be non-negative")
    private BigDecimal acctCurrCycDebit;

    @Size(max = 10, message = "Address ZIP cannot exceed 10 characters")
    private String acctAddrZip;

    @Size(max = 10, message = "Group ID cannot exceed 10 characters")
    private String acctGroupId;

    @NotBlank(message = "Customer first name cannot be empty")
    private String customerFirstName;

    @NotBlank(message = "Customer last name cannot be empty")
    private String customerLastName;

    @Pattern(regexp = "\\d{3}-\\d{2}-\\d{4}", message = "SSN must be in XXX-XX-XXXX format")
    private String customerSsn;

    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "Date must be in YYYY-MM-DD format")
    private String customerDateOfBirth;

    @Min(value = 300, message = "FICO score must be at least 300")
    @Max(value = 850, message = "FICO score cannot exceed 850")
    private Integer customerFicoScore;

    private String customerPhone1;
    private String customerPhone2;
    private String customerAddress1;
    private String customerAddress2;
    private String customerCity;
    private String customerState;
    private String customerZipCode;
    private String customerCountry;
    private String customerGovtIssuedId;
    private String customerEftAccountId;
    private String customerPriCardHolderInd;

    public AccountUpdateRequest() {}

    public String getAcctActiveStatus() { return acctActiveStatus; }
    public void setAcctActiveStatus(String acctActiveStatus) { this.acctActiveStatus = acctActiveStatus; }

    public BigDecimal getAcctCurrBal() { return acctCurrBal; }
    public void setAcctCurrBal(BigDecimal acctCurrBal) { this.acctCurrBal = acctCurrBal; }

    public BigDecimal getAcctCreditLimit() { return acctCreditLimit; }
    public void setAcctCreditLimit(BigDecimal acctCreditLimit) { this.acctCreditLimit = acctCreditLimit; }

    public BigDecimal getAcctCashCreditLimit() { return acctCashCreditLimit; }
    public void setAcctCashCreditLimit(BigDecimal acctCashCreditLimit) { this.acctCashCreditLimit = acctCashCreditLimit; }

    public String getAcctOpenDate() { return acctOpenDate; }
    public void setAcctOpenDate(String acctOpenDate) { this.acctOpenDate = acctOpenDate; }

    public String getAcctExpirationDate() { return acctExpirationDate; }
    public void setAcctExpirationDate(String acctExpirationDate) { this.acctExpirationDate = acctExpirationDate; }

    public String getAcctReissueDate() { return acctReissueDate; }
    public void setAcctReissueDate(String acctReissueDate) { this.acctReissueDate = acctReissueDate; }

    public BigDecimal getAcctCurrCycCredit() { return acctCurrCycCredit; }
    public void setAcctCurrCycCredit(BigDecimal acctCurrCycCredit) { this.acctCurrCycCredit = acctCurrCycCredit; }

    public BigDecimal getAcctCurrCycDebit() { return acctCurrCycDebit; }
    public void setAcctCurrCycDebit(BigDecimal acctCurrCycDebit) { this.acctCurrCycDebit = acctCurrCycDebit; }

    public String getAcctAddrZip() { return acctAddrZip; }
    public void setAcctAddrZip(String acctAddrZip) { this.acctAddrZip = acctAddrZip; }

    public String getAcctGroupId() { return acctGroupId; }
    public void setAcctGroupId(String acctGroupId) { this.acctGroupId = acctGroupId; }

    public String getCustomerFirstName() { return customerFirstName; }
    public void setCustomerFirstName(String customerFirstName) { this.customerFirstName = customerFirstName; }

    public String getCustomerLastName() { return customerLastName; }
    public void setCustomerLastName(String customerLastName) { this.customerLastName = customerLastName; }

    public String getCustomerSsn() { return customerSsn; }
    public void setCustomerSsn(String customerSsn) { this.customerSsn = customerSsn; }

    public String getCustomerDateOfBirth() { return customerDateOfBirth; }
    public void setCustomerDateOfBirth(String customerDateOfBirth) { this.customerDateOfBirth = customerDateOfBirth; }

    public Integer getCustomerFicoScore() { return customerFicoScore; }
    public void setCustomerFicoScore(Integer customerFicoScore) { this.customerFicoScore = customerFicoScore; }

    public String getCustomerPhone1() { return customerPhone1; }
    public void setCustomerPhone1(String customerPhone1) { this.customerPhone1 = customerPhone1; }

    public String getCustomerPhone2() { return customerPhone2; }
    public void setCustomerPhone2(String customerPhone2) { this.customerPhone2 = customerPhone2; }

    public String getCustomerAddress1() { return customerAddress1; }
    public void setCustomerAddress1(String customerAddress1) { this.customerAddress1 = customerAddress1; }

    public String getCustomerAddress2() { return customerAddress2; }
    public void setCustomerAddress2(String customerAddress2) { this.customerAddress2 = customerAddress2; }

    public String getCustomerCity() { return customerCity; }
    public void setCustomerCity(String customerCity) { this.customerCity = customerCity; }

    public String getCustomerState() { return customerState; }
    public void setCustomerState(String customerState) { this.customerState = customerState; }

    public String getCustomerZipCode() { return customerZipCode; }
    public void setCustomerZipCode(String customerZipCode) { this.customerZipCode = customerZipCode; }

    public String getCustomerCountry() { return customerCountry; }
    public void setCustomerCountry(String customerCountry) { this.customerCountry = customerCountry; }

    public String getCustomerGovtIssuedId() { return customerGovtIssuedId; }
    public void setCustomerGovtIssuedId(String customerGovtIssuedId) { this.customerGovtIssuedId = customerGovtIssuedId; }

    public String getCustomerEftAccountId() { return customerEftAccountId; }
    public void setCustomerEftAccountId(String customerEftAccountId) { this.customerEftAccountId = customerEftAccountId; }

    public String getCustomerPriCardHolderInd() { return customerPriCardHolderInd; }
    public void setCustomerPriCardHolderInd(String customerPriCardHolderInd) { this.customerPriCardHolderInd = customerPriCardHolderInd; }
}
