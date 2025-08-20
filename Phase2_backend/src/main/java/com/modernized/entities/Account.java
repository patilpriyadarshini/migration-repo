package com.modernized.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

/**
 * Credit accounts that hold balances, credit limits, and billing information
 * Source: CVACT01Y.cpy, lines 4-17
 */
@Entity
@Table(name = "account")
public class Account {

    @Id
    @NotNull
    @Column(name = "acct_id")
    private Long acctId;

    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "acct_active_status", length = 1)
    private String acctActiveStatus;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = true)
    @Column(name = "acct_curr_bal", precision = 12, scale = 2)
    private BigDecimal acctCurrBal;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    @Column(name = "acct_credit_limit", precision = 12, scale = 2)
    private BigDecimal acctCreditLimit;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = true)
    @Column(name = "acct_cash_credit_limit", precision = 12, scale = 2)
    private BigDecimal acctCashCreditLimit;

    @NotNull
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "Date must be in YYYY-MM-DD format")
    @Column(name = "acct_open_date", length = 10)
    private String acctOpenDate;

    @NotNull
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "Date must be in YYYY-MM-DD format")
    @Column(name = "acct_expiraion_date", length = 10)
    private String acctExpiraionDate;

    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "Date must be in YYYY-MM-DD format")
    @Column(name = "acct_reissue_date", length = 10)
    private String acctReissueDate;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = true)
    @Column(name = "acct_curr_cyc_credit", precision = 12, scale = 2)
    private BigDecimal acctCurrCycCredit;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = true)
    @Column(name = "acct_curr_cyc_debit", precision = 12, scale = 2)
    private BigDecimal acctCurrCycDebit;

    @Column(name = "acct_addr_zip", length = 10)
    private String acctAddrZip;

    @Column(name = "acct_group_id", length = 10)
    private String acctGroupId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "acct_cust_id", referencedColumnName = "cust_id")
    private Customer customer;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Card> cards;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TransactionCategoryBalance> transactionCategoryBalances;


    public Account() {}

    public Account(Long acctId, String acctActiveStatus, BigDecimal acctCurrBal,
                  BigDecimal acctCreditLimit, BigDecimal acctCashCreditLimit,
                  String acctOpenDate, String acctExpiraionDate, String acctReissueDate,
                  BigDecimal acctCurrCycCredit, BigDecimal acctCurrCycDebit,
                  String acctAddrZip, String acctGroupId) {
        this.acctId = acctId;
        this.acctActiveStatus = acctActiveStatus;
        this.acctCurrBal = acctCurrBal;
        this.acctCreditLimit = acctCreditLimit;
        this.acctCashCreditLimit = acctCashCreditLimit;
        this.acctOpenDate = acctOpenDate;
        this.acctExpiraionDate = acctExpiraionDate;
        this.acctReissueDate = acctReissueDate;
        this.acctCurrCycCredit = acctCurrCycCredit;
        this.acctCurrCycDebit = acctCurrCycDebit;
        this.acctAddrZip = acctAddrZip;
        this.acctGroupId = acctGroupId;
    }

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

    public String getAcctExpiraionDate() { return acctExpiraionDate; }
    public void setAcctExpiraionDate(String acctExpiraionDate) { this.acctExpiraionDate = acctExpiraionDate; }

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

    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }

    public List<Card> getCards() { return cards; }
    public void setCards(List<Card> cards) { this.cards = cards; }

    public List<TransactionCategoryBalance> getTransactionCategoryBalances() { return transactionCategoryBalances; }
    public void setTransactionCategoryBalances(List<TransactionCategoryBalance> transactionCategoryBalances) { this.transactionCategoryBalances = transactionCategoryBalances; }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(acctId, account.acctId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(acctId);
    }

    @Override
    public String toString() {
        return "Account{" +
                "acctId=" + acctId +
                ", acctActiveStatus='" + acctActiveStatus + '\'' +
                ", acctCurrBal=" + acctCurrBal +
                '}';
    }
}
