package com.modernized.entities;

import jakarta.persistence.*;
import java.util.List;
import java.util.Objects;

/**
 * Physical or virtual payment cards linked to credit accounts for transaction processing
 * Source: CVACT02Y.cpy, lines 4-11
 */
@Entity
@Table(name = "card")
public class Card {

    @Id
    @Column(name = "card_num", length = 16)
    private String cardNum;

    @Column(name = "card_acct_id")
    private Long cardAcctId;

    @Column(name = "card_cvv_cd")
    private Integer cardCvvCd;

    @Column(name = "card_embossed_name", length = 50)
    private String cardEmbossedName;

    @Column(name = "card_expiration_date", length = 10)
    private String cardExpirationDate;

    @Column(name = "card_active_status", length = 1)
    private String cardActiveStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_acct_id", referencedColumnName = "acct_id", insertable = false, updatable = false)
    private Account account;

    @OneToMany(mappedBy = "card", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Transaction> transactions;

    @OneToMany(mappedBy = "card", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DailyTransaction> dailyTransactions;

    @OneToOne(mappedBy = "card", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private CardCrossReference cardCrossReference;

    public Card() {}

    public Card(String cardNum, Long cardAcctId, Integer cardCvvCd,
               String cardEmbossedName, String cardExpirationDate, String cardActiveStatus) {
        this.cardNum = cardNum;
        this.cardAcctId = cardAcctId;
        this.cardCvvCd = cardCvvCd;
        this.cardEmbossedName = cardEmbossedName;
        this.cardExpirationDate = cardExpirationDate;
        this.cardActiveStatus = cardActiveStatus;
    }

    public String getCardNum() { return cardNum; }
    public void setCardNum(String cardNum) { this.cardNum = cardNum; }

    public Long getCardAcctId() { return cardAcctId; }
    public void setCardAcctId(Long cardAcctId) { this.cardAcctId = cardAcctId; }

    public Integer getCardCvvCd() { return cardCvvCd; }
    public void setCardCvvCd(Integer cardCvvCd) { this.cardCvvCd = cardCvvCd; }

    public String getCardEmbossedName() { return cardEmbossedName; }
    public void setCardEmbossedName(String cardEmbossedName) { this.cardEmbossedName = cardEmbossedName; }

    public String getCardExpirationDate() { return cardExpirationDate; }
    public void setCardExpirationDate(String cardExpirationDate) { this.cardExpirationDate = cardExpirationDate; }

    public String getCardActiveStatus() { return cardActiveStatus; }
    public void setCardActiveStatus(String cardActiveStatus) { this.cardActiveStatus = cardActiveStatus; }

    public Account getAccount() { return account; }
    public void setAccount(Account account) { this.account = account; }

    public List<Transaction> getTransactions() { return transactions; }
    public void setTransactions(List<Transaction> transactions) { this.transactions = transactions; }

    public List<DailyTransaction> getDailyTransactions() { return dailyTransactions; }
    public void setDailyTransactions(List<DailyTransaction> dailyTransactions) { this.dailyTransactions = dailyTransactions; }

    public CardCrossReference getCardCrossReference() { return cardCrossReference; }
    public void setCardCrossReference(CardCrossReference cardCrossReference) { this.cardCrossReference = cardCrossReference; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return Objects.equals(cardNum, card.cardNum);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cardNum);
    }

    @Override
    public String toString() {
        return "Card{" +
                "cardNum='" + cardNum + '\'' +
                ", cardAcctId=" + cardAcctId +
                ", cardActiveStatus='" + cardActiveStatus + '\'' +
                '}';
    }
}
