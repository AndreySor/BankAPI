package com.sber.services;

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
        DataSource ds = ServiceDataSource.getDataSource();

        try (Connection conn = ds.getConnection()) {
            try (Statement statement = conn.createStatement()) {

                statement.execute("DROP TABLE IF EXISTS sber_cards");
                statement.execute("DROP TABLE IF EXISTS sber_accounts");
                statement.execute("DROP TABLE IF EXISTS sber_users");

                statement.execute("CREATE TABLE sber_users (\n" +
                        "    user_id BIGSERIAL PRIMARY KEY,\n" +
                        "    first_name VARCHAR(50),\n" +
                        "    last_name VARCHAR(50)\n" +
                        ");");
                statement.execute("CREATE TABLE sber_accounts (\n" +
                        "    account_id BIGSERIAL PRIMARY KEY,\n" +
                        "    account_number VARCHAR(30),\n" +
                        "    balance DECIMAL(15,2),\n" +
                        "    owner_id BIGINT REFERENCES sber_users(user_id) ON DELETE CASCADE\n" +
                        ");");
                statement.execute("CREATE TABLE sber_cards (\n" +
                        "    card_id BIGSERIAL PRIMARY KEY,\n" +
                        "    card_number VARCHAR(30),\n" +
                        "    account_id BIGINT REFERENCES sber_accounts(account_id) ON DELETE CASCADE,\n" +
                        "    owner_id BIGINT REFERENCES sber_users(user_id) ON DELETE CASCADE\n" +
                        ")");

                statement.execute("INSERT INTO sber_users(first_name, last_name) VALUES ('Andrey', 'Sidorov')");
                statement.execute("INSERT INTO sber_users(first_name, last_name) VALUES ('Sergey', 'Larin')");
                statement.execute("INSERT INTO sber_users(first_name, last_name) VALUES ('Anton', 'Zubov')");
                statement.execute("INSERT INTO sber_users(first_name, last_name) VALUES ('Zaur', 'Ivanov')");
                statement.execute("INSERT INTO sber_users(first_name, last_name) VALUES ('Marsel', 'Abramov')");
                statement.execute("INSERT INTO sber_accounts(account_number, owner_id, balance) VALUES ('4356 3245 1234 7345', 5, 245.30)");
                statement.execute("INSERT INTO sber_accounts(account_number, owner_id, balance) VALUES ('4356 3834 1234 2855', 1, 30045.23)");
                statement.execute("INSERT INTO sber_accounts(account_number, owner_id, balance) VALUES ('7356 9264 7634 2534', 3, 2000.00)");
                statement.execute("INSERT INTO sber_accounts(account_number, owner_id, balance) VALUES ('2054 6334 2299 8376', 2, 7043.54)");
                statement.execute("INSERT INTO sber_accounts(account_number, owner_id, balance) VALUES ('9934 9264 3456 4423', 4, 12900.40)");
                statement.execute("INSERT INTO sber_cards(card_number, account_id, owner_id) VALUES ('4356 3245 1234 7345', 1, 5)");
                statement.execute("INSERT INTO sber_cards(card_number, account_id, owner_id) VALUES ('4356 3834 1234 2855', 2, 1)");
                statement.execute("INSERT INTO sber_cards(card_number, account_id, owner_id) VALUES ('7356 9264 7634 2534', 3, 3)");
                statement.execute("INSERT INTO sber_cards(card_number, account_id, owner_id) VALUES ('2054 6334 2299 8376', 4, 2)");
                statement.execute("INSERT INTO sber_cards(card_number, account_id, owner_id) VALUES ('9934 9264 3456 4423', 5, 4)");
            }
        }
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
