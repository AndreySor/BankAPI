package com.sber.models;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Objects;

@JsonAutoDetect
public class Card {
    private Long cardId;
    private String cardNumber;
    @JsonIgnore
    private Account account;
    @JsonIgnore
    private User owner;

    private Card(Builder builder) {
        this.cardId = builder.cardId;
        this.cardNumber = builder.cardNumber;
        this.account = builder.account;
        this.owner = builder.owner;
    }

    public static class Builder {
        private Long cardId;
        private String cardNumber;
        private Account account;
        private User owner;

        public Builder cardId(Long cardId) {
            this.cardId = cardId;
            return this;
        }

        public Builder cardNumber(String cardNumber) {
            this.cardNumber = cardNumber;
            return this;
        }

        public Builder account(Account account) {
            this.account = account;
            return this;
        }

        public Builder owner(User owner) {
            this.owner = owner;
            return this;
        }

        public Card build() {
            return new Card(this);
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public Long getCardId() {
        return cardId;
    }

    public void setCardId(Long cardId) {
        this.cardId = cardId;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String number) {
        this.cardNumber = number;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return Objects.equals(cardId, card.cardId) &&
                Objects.equals(cardNumber, card.cardNumber) &&
                Objects.equals(account, card.account) &&
                Objects.equals(owner, card.owner);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cardId, cardNumber, account, owner);
    }

    @Override
    public String toString() {
        return "Card{" +
                "cardId=" + cardId +
                ", cardNumber='" + cardNumber + '\'' +
                ", account=" + account.getAccountNumber() +
                ", owner=" + owner.getFirstName() + " " + owner.getLastName().charAt(0) + "." +
                '}';
    }
}
