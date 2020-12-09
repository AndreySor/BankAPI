package com.sber.services;

import com.sber.models.Account;
import com.sber.repositories.AccountRepositoryImp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class AccountServiceImpTests {

    @Test
    void isBalanceReplenishment() {
        Account account = Account.builder()
                .accountNumber("2054 6334 2299 8376")
                .balance(new BigDecimal("1000"))
                .build();

        AccountServiceImp accountServiceImp = new AccountServiceImp();
        String result = accountServiceImp.balanceReplenishment(account);
        assertEquals(result, "SUCCESS");
    }

    @Test
    void isBalanceReplenishmentCheckBalance() {
        Account account = Account.builder()
                .accountNumber("2054 6334 2299 8376")
                .balance(new BigDecimal("1000"))
                .build();

        AccountServiceImp accountServiceImp = new AccountServiceImp();
        accountServiceImp.balanceReplenishment(account);
        AccountRepositoryImp accountRepositoryImp = new AccountRepositoryImp(ServiceDataSource.getDataSource());

        try {
        BigDecimal newBalanse = accountRepositoryImp.get(4L).get().getBalance();
        assertEquals(newBalanse, new BigDecimal("9043.54"));
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    void isBalanceReplenishmentCheckBalanceNegative() {
        Account account = Account.builder()
                .accountNumber("2054 6334 2299 8376")
                .balance(new BigDecimal("-1000"))
                .build();

        AccountServiceImp accountServiceImp = new AccountServiceImp();
        String result = accountServiceImp.balanceReplenishment(account);
        assertEquals(result, "UNSUCCESS");
    }

    @Test
    void isBalanceReplenishmentCheckBalanceZero() {
        Account account = Account.builder()
                .accountNumber("2054 6334 2299 8376")
                .balance(new BigDecimal("0"))
                .build();

        AccountServiceImp accountServiceImp = new AccountServiceImp();
        String result = accountServiceImp.balanceReplenishment(account);
        assertEquals(result, "SUCCESS");
    }

    @Test
    void isBalanceReplenishmentAccountNumberWrong() {
        Account account = Account.builder()
                .accountNumber("2054 6334 2299 0000")
                .balance(new BigDecimal("1000"))
                .build();

        AccountServiceImp accountServiceImp = new AccountServiceImp();
        String result = accountServiceImp.balanceReplenishment(account);
        assertEquals(result, "UNSUCCESS");
    }

    @Test
    void isBalanceReplenishmentAccountIsNull() {
        AccountServiceImp accountServiceImp = new AccountServiceImp();
        String result = accountServiceImp.balanceReplenishment(null);
        assertEquals(result, "UNSUCCESS");
    }

    @Test
    void isBalanceReplenishmentAccountNumberIsNull() {
        Account account = Account.builder()
                .accountNumber(null)
                .balance(new BigDecimal("1000"))
                .build();

        AccountServiceImp accountServiceImp = new AccountServiceImp();
        String result = accountServiceImp.balanceReplenishment(account);
        assertEquals(result, "UNSUCCESS");
    }

    @Test
    void isBalanceReplenishmentAccountNumberIsEmpty() {
        Account account = Account.builder()
                .accountNumber("")
                .balance(new BigDecimal("1000"))
                .build();

        AccountServiceImp accountServiceImp = new AccountServiceImp();
        String result = accountServiceImp.balanceReplenishment(account);
        assertEquals(result, "UNSUCCESS");
    }

    @Test
    void isCheckingBalance() {
        Account account = Account.builder()
                .accountNumber("2054 6334 2299 8376")
                .balance(new BigDecimal("1000"))
                .build();

        AccountServiceImp accountServiceImp = new AccountServiceImp();
        BigDecimal result = accountServiceImp.checkingBalance(account);
        assertEquals(result, new BigDecimal("7043.54"));
    }

    @Test
    void isCheckingBalanceWrong() {
        Account account = Account.builder()
                .accountNumber("2054 6334 2299 0000")
                .balance(new BigDecimal("1000"))
                .build();

        AccountServiceImp accountServiceImp = new AccountServiceImp();
        BigDecimal result = accountServiceImp.checkingBalance(account);
        assertNull(result);
    }

    @Test
    void isCheckingBalanceAccountIsNull() {
        AccountServiceImp accountServiceImp = new AccountServiceImp();
        BigDecimal result = accountServiceImp.checkingBalance(null);
        assertNull(result);
    }

    @Test
    void isCheckingBalanceAccountNumberIsNull() {
        Account account = Account.builder()
                .accountNumber(null)
                .balance(new BigDecimal("1000"))
                .build();

        AccountServiceImp accountServiceImp = new AccountServiceImp();
        BigDecimal result = accountServiceImp.checkingBalance(account);
        assertNull(result);
    }

    @Test
    void isCheckingBalanceAccountNumberIsEmpty() {
        Account account = Account.builder()
                .accountNumber("")
                .balance(new BigDecimal("1000"))
                .build();

        AccountServiceImp accountServiceImp = new AccountServiceImp();
        BigDecimal result = accountServiceImp.checkingBalance(account);
        assertNull(result);
    }
}
