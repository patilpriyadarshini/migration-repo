package com.modernized.entities;

import jakarta.persistence.*;
import java.util.Objects;

/**
 * Junction entity linking customers to their accounts and cards for authorization and management
 * Source: cardxref.txt reference data structure
 */
@Entity
@Table(name = "card_cross_reference")
public class CardCrossReference {

    @Id
    @Column(name = "xref_card_num", length = 16)
    private String xrefCardNum;

    @Column(name = "xref_cust_id")
    private Long xrefCustId;

    @Column(name = "xref_acct_id")
    private Long xrefAcctId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "xref_cust_id", referencedColumnName = "cust_id", insertable = false, updatable = false)
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "xref_acct_id", referencedColumnName = "acct_id", insertable = false, updatable = false)
    private Account account;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "xref_card_num", referencedColumnName = "card_num", insertable = false, updatable = false)
    private Card card;

    public CardCrossReference() {}

    public CardCrossReference(String xrefCardNum, Long xrefCustId, Long xrefAcctId) {
        this.xrefCardNum = xrefCardNum;
        this.xrefCustId = xrefCustId;
        this.xrefAcctId = xrefAcctId;
    }

    public String getXrefCardNum() { return xrefCardNum; }
    public void setXrefCardNum(String xrefCardNum) { this.xrefCardNum = xrefCardNum; }

    public Long getXrefCustId() { return xrefCustId; }
    public void setXrefCustId(Long xrefCustId) { this.xrefCustId = xrefCustId; }

    public Long getXrefAcctId() { return xrefAcctId; }
    public void setXrefAcctId(Long xrefAcctId) { this.xrefAcctId = xrefAcctId; }

    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }

    public Account getAccount() { return account; }
    public void setAccount(Account account) { this.account = account; }

    public Card getCard() { return card; }
    public void setCard(Card card) { this.card = card; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CardCrossReference that = (CardCrossReference) o;
        return Objects.equals(xrefCardNum, that.xrefCardNum);
    }

    @Override
    public int hashCode() {
        return Objects.hash(xrefCardNum);
    }

    @Override
    public String toString() {
        return "CardCrossReference{" +
                "xrefCardNum='" + xrefCardNum + '\'' +
                ", xrefCustId=" + xrefCustId +
                ", xrefAcctId=" + xrefAcctId +
                '}';
    }
}
