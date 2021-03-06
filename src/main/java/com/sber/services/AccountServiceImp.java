package com.sber.services;

import com.sber.models.Account;
import com.sber.repositories.AccountRepository;
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
            AccountRepository accountRepositoryImp = new AccountRepositoryImp(ServiceDataSource.getDataSource());
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
                throw new NotSavedSubEntityException("failed to top up your balance");
            }
        }
        return UNSUCCESS;
    }

    @Override
    public Account checkingBalance(Account account) {
        if (account != null && account.getAccountId() != null && account.getAccountId() !=0L) {
            AccountRepository accountRepositoryImp = new AccountRepositoryImp(ServiceDataSource.getDataSource());
            try {
                Optional<Account> changeAccount = accountRepositoryImp.get(account.getAccountId());
                if (changeAccount.isPresent()) {
                    return changeAccount.get();
                }
            } catch (SQLException ex) {
                log.log(Level.SEVERE, "Exception: ", ex);
                throw new NotSavedSubEntityException("failed to check balance");
            }
        }
        return null;
    }
}
