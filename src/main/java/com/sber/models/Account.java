package com.sber.models;

import java.math.BigDecimal;
import java.util.Objects;

public class Account {
    private Long accountId;
    private String accountNumber;
    private BigDecimal balance;
    private User owner;

    private Account(Builder builder) {
        this.accountId = builder.accountId;
        this.accountNumber = builder.accountNumber;
        this.balance = builder.balance.setScale(2);
        this.owner = builder.owner;
    }

    public static class Builder {
        private Long accountId;
        private String accountNumber;
        private BigDecimal balance;
        private User owner;

        public Builder accountId(Long accountId) {
            this.accountId = accountId;
            return this;
        }

        public Builder accountNumber(String accountNumber) {
            this.accountNumber = accountNumber;
            return this;
        }

        public Builder balance(BigDecimal balance) {
            this.balance = balance;
            return this;
        }

        public Builder owner(User owner) {
            this.owner = owner;
            return this;
        }

        public Account build() {
            return new Account(this);
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
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
        Account account = (Account) o;
        return Objects.equals(accountId, account.accountId) &&
                Objects.equals(accountNumber, account.accountNumber) &&
                Objects.equals(balance, account.balance) &&
                Objects.equals(owner, account.owner);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountId, accountNumber, balance, owner);
    }

    @Override
    public String toString() {
        return "Account{" +
                "accountId=" + accountId +
                ", accountNumber='" + accountNumber + '\'' +
                ", balance=" + balance +
                ", owner=" + owner +
                '}';
    }
}
