package com.sber.services;

import com.sber.models.Account;

import java.math.BigDecimal;

public interface AccountService {
    public String balanceReplenishment(Account account);
    public Account checkingBalance(Account account);
}
