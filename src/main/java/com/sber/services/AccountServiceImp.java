package com.sber.services;

import com.sber.models.Account;
import com.sber.repositories.AccountRepositoryImp;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AccountServiceImp implements AccountService{

    private static Logger log = Logger.getLogger(AccountServiceImp.class.getName());

    final String SUCCESS = "SUCCESS";
    final String UNSUCCESS = "UNSUCCESS";

    @Override
    public String balanceReplenishment(Account account) {
        if (account != null && account.getBalance().compareTo(new BigDecimal(0)) >= 0 &&
                (account.getAccountNumber() != null && !account.getAccountNumber().isEmpty())) {
            AccountRepositoryImp accountRepositoryImp = new AccountRepositoryImp(ServiceDataSource.getDataSource());
            try {
                Optional changeAccount = accountRepositoryImp.getByNumber(account.getAccountNumber());
                if (changeAccount.isPresent()) {
                    Account newBalance = (Account) changeAccount.get();
                    newBalance.setBalance(newBalance.getBalance().add(account.getBalance()));
                    accountRepositoryImp.update(newBalance);
                    return SUCCESS;
                }
            } catch (SQLException ex) {
                log.log(Level.SEVERE, "Exception: ", ex);
            }
        }
        return UNSUCCESS;
    }

    @Override
    public BigDecimal checkingBalance(Account account) {
        if (account != null && (account.getAccountNumber() != null && !account.getAccountNumber().isEmpty())) {
            AccountRepositoryImp accountRepositoryImp = new AccountRepositoryImp(ServiceDataSource.getDataSource());
            try {
                Optional changeAccount = accountRepositoryImp.getByNumber(account.getAccountNumber());
                if (changeAccount.isPresent()) {
                    Account checkBalance = (Account) changeAccount.get();
                    return checkBalance.getBalance();
                }
            } catch (SQLException ex) {
                log.log(Level.SEVERE, "Exception: ", ex);
            }
        }
        return null;
    }
}
