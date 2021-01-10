package com.sber.services;

import com.sber.FullDatabase;
import com.sber.models.Account;
import com.sber.repositories.AccountRepositoryImp;
import org.h2.jdbcx.JdbcDataSource;
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

    @BeforeEach
    public void init() throws SQLException {
        new FullDatabase().fullDatabase();
    }

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
        assertEquals(newBalanse, new BigDecimal("8043.54"));
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
                .accountId(4L)
                .accountNumber("2054 6334 2299 8376")
                .balance(new BigDecimal("1000"))
                .build();

        AccountServiceImp accountServiceImp = new AccountServiceImp();
        Account result = accountServiceImp.checkingBalance(account);
        assertEquals(result.getBalance(), new BigDecimal("7043.54"));
    }

    @Test
    void isCheckingBalanceAccountIdIsNull() {
        Account account = Account.builder()
                .accountId(null)
                .accountNumber("2054 6334 2299 0000")
                .balance(new BigDecimal("1000"))
                .build();

        AccountServiceImp accountServiceImp = new AccountServiceImp();
        Account result = accountServiceImp.checkingBalance(account);
        assertNull(result);
    }

    @Test
    void isCheckingBalanceAccountIdIsZero() {
        Account account = Account.builder()
                .accountId(0L)
                .accountNumber("2054 6334 2299 0000")
                .balance(new BigDecimal("1000"))
                .build();

        AccountServiceImp accountServiceImp = new AccountServiceImp();
        Account result = accountServiceImp.checkingBalance(account);
        assertNull(result);
    }

    @Test
    void isCheckingBalanceAccountIsNull() {
        AccountServiceImp accountServiceImp = new AccountServiceImp();
        Account result = accountServiceImp.checkingBalance(null);
        assertNull(result);
    }
}
