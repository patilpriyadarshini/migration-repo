package com.modernized.entities;

import jakarta.persistence.*;
import java.util.List;
import java.util.Objects;

/**
 * Individual credit card holders who own accounts and use payment services
 * Source: CVCUS01Y.cpy, lines 4-23
 */
@Entity
@Table(name = "customer")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cust_id")
    private Long custId;

    @Column(name = "cust_first_name", length = 25)
    private String custFirstName;

    @Column(name = "cust_middle_name", length = 25)
    private String custMiddleName;

    @Column(name = "cust_last_name", length = 25)
    private String custLastName;

    @Column(name = "cust_addr_line_1", length = 50)
    private String custAddrLine1;

    @Column(name = "cust_addr_line_2", length = 50)
    private String custAddrLine2;

    @Column(name = "cust_addr_line_3", length = 50)
    private String custAddrLine3;

    @Column(name = "cust_addr_state_cd", length = 2)
    private String custAddrStateCd;

    @Column(name = "cust_addr_country_cd", length = 3)
    private String custAddrCountryCd;

    @Column(name = "cust_addr_zip", length = 10)
    private String custAddrZip;

    @Column(name = "cust_phone_num_1", length = 15)
    private String custPhoneNum1;

    @Column(name = "cust_phone_num_2", length = 15)
    private String custPhoneNum2;

    @Column(name = "cust_ssn")
    private Long custSsn;

    @Column(name = "cust_govt_issued_id", length = 20)
    private String custGovtIssuedId;

    @Column(name = "cust_dob_yyyy_mm_dd", length = 10)
    private String custDobYyyyMmDd;

    @Column(name = "cust_eft_account_id", length = 10)
    private String custEftAccountId;

    @Column(name = "cust_pri_card_holder_ind", length = 1)
    private String custPriCardHolderInd;

    @Column(name = "cust_fico_credit_score")
    private Integer custFicoCreditScore;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Account> accounts;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CardCrossReference> cardCrossReferences;

    public Customer() {}

    public Customer(Long custId, String custFirstName, String custMiddleName, String custLastName,
                   String custAddrLine1, String custAddrLine2, String custAddrLine3,
                   String custAddrStateCd, String custAddrCountryCd, String custAddrZip,
                   String custPhoneNum1, String custPhoneNum2, Long custSsn,
                   String custGovtIssuedId, String custDobYyyyMmDd, String custEftAccountId,
                   String custPriCardHolderInd, Integer custFicoCreditScore) {
        this.custId = custId;
        this.custFirstName = custFirstName;
        this.custMiddleName = custMiddleName;
        this.custLastName = custLastName;
        this.custAddrLine1 = custAddrLine1;
        this.custAddrLine2 = custAddrLine2;
        this.custAddrLine3 = custAddrLine3;
        this.custAddrStateCd = custAddrStateCd;
        this.custAddrCountryCd = custAddrCountryCd;
        this.custAddrZip = custAddrZip;
        this.custPhoneNum1 = custPhoneNum1;
        this.custPhoneNum2 = custPhoneNum2;
        this.custSsn = custSsn;
        this.custGovtIssuedId = custGovtIssuedId;
        this.custDobYyyyMmDd = custDobYyyyMmDd;
        this.custEftAccountId = custEftAccountId;
        this.custPriCardHolderInd = custPriCardHolderInd;
        this.custFicoCreditScore = custFicoCreditScore;
    }

    public Long getCustId() { return custId; }
    public void setCustId(Long custId) { this.custId = custId; }

    public String getCustFirstName() { return custFirstName; }
    public void setCustFirstName(String custFirstName) { this.custFirstName = custFirstName; }

    public String getCustMiddleName() { return custMiddleName; }
    public void setCustMiddleName(String custMiddleName) { this.custMiddleName = custMiddleName; }

    public String getCustLastName() { return custLastName; }
    public void setCustLastName(String custLastName) { this.custLastName = custLastName; }

    public String getCustAddrLine1() { return custAddrLine1; }
    public void setCustAddrLine1(String custAddrLine1) { this.custAddrLine1 = custAddrLine1; }

    public String getCustAddrLine2() { return custAddrLine2; }
    public void setCustAddrLine2(String custAddrLine2) { this.custAddrLine2 = custAddrLine2; }

    public String getCustAddrLine3() { return custAddrLine3; }
    public void setCustAddrLine3(String custAddrLine3) { this.custAddrLine3 = custAddrLine3; }

    public String getCustAddrStateCd() { return custAddrStateCd; }
    public void setCustAddrStateCd(String custAddrStateCd) { this.custAddrStateCd = custAddrStateCd; }

    public String getCustAddrCountryCd() { return custAddrCountryCd; }
    public void setCustAddrCountryCd(String custAddrCountryCd) { this.custAddrCountryCd = custAddrCountryCd; }

    public String getCustAddrZip() { return custAddrZip; }
    public void setCustAddrZip(String custAddrZip) { this.custAddrZip = custAddrZip; }

    public String getCustPhoneNum1() { return custPhoneNum1; }
    public void setCustPhoneNum1(String custPhoneNum1) { this.custPhoneNum1 = custPhoneNum1; }

    public String getCustPhoneNum2() { return custPhoneNum2; }
    public void setCustPhoneNum2(String custPhoneNum2) { this.custPhoneNum2 = custPhoneNum2; }

    public Long getCustSsn() { return custSsn; }
    public void setCustSsn(Long custSsn) { this.custSsn = custSsn; }

    public String getCustGovtIssuedId() { return custGovtIssuedId; }
    public void setCustGovtIssuedId(String custGovtIssuedId) { this.custGovtIssuedId = custGovtIssuedId; }

    public String getCustDobYyyyMmDd() { return custDobYyyyMmDd; }
    public void setCustDobYyyyMmDd(String custDobYyyyMmDd) { this.custDobYyyyMmDd = custDobYyyyMmDd; }

    public String getCustEftAccountId() { return custEftAccountId; }
    public void setCustEftAccountId(String custEftAccountId) { this.custEftAccountId = custEftAccountId; }

    public String getCustPriCardHolderInd() { return custPriCardHolderInd; }
    public void setCustPriCardHolderInd(String custPriCardHolderInd) { this.custPriCardHolderInd = custPriCardHolderInd; }

    public Integer getCustFicoCreditScore() { return custFicoCreditScore; }
    public void setCustFicoCreditScore(Integer custFicoCreditScore) { this.custFicoCreditScore = custFicoCreditScore; }

    public List<Account> getAccounts() { return accounts; }
    public void setAccounts(List<Account> accounts) { this.accounts = accounts; }

    public List<CardCrossReference> getCardCrossReferences() { return cardCrossReferences; }
    public void setCardCrossReferences(List<CardCrossReference> cardCrossReferences) { this.cardCrossReferences = cardCrossReferences; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return Objects.equals(custId, customer.custId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(custId);
    }

    @Override
    public String toString() {
        return "Customer{" +
                "custId=" + custId +
                ", custFirstName='" + custFirstName + '\'' +
                ", custLastName='" + custLastName + '\'' +
                '}';
    }
}
