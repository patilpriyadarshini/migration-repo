package com.modernized.dto;

import java.math.BigDecimal;

public class AccountResponse {
    private Long acctId;
    private String acctActiveStatus;
    private BigDecimal acctCurrBal;
    private BigDecimal acctCreditLimit;
    private BigDecimal acctCashCreditLimit;
    private String acctOpenDate;
    private String acctExpirationDate;
    private String acctReissueDate;
    private BigDecimal acctCurrCycCredit;
    private BigDecimal acctCurrCycDebit;
    private String acctAddrZip;
    private String acctGroupId;
    
    private String customerFirstName;
    private String customerLastName;
    private String customerSsn;
    private String customerDateOfBirth;
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

    public AccountResponse() {}

    public Long getAcctId() { return acctId; }
    public void setAcctId(Long acctId) { this.acctId = acctId; }

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
