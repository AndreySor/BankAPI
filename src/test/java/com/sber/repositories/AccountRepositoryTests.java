package com.sber.repositories;

import com.sber.FullDatabase;
import com.sber.models.Account;
import com.sber.models.User;
import com.sber.services.ServiceDataSource;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.*;

public class AccountRepositoryTests {
    DataSource dataSource;

    final  Account EXPECTED_GET_BY_ID = Account.builder()
                    .accountId(3L)
                    .accountNumber("7356 9264 7634 2534")
                    .balance(new BigDecimal("2000.00"))
                    .owner(User.builder()
                            .userId(3L)
                            .firstName("Anton")
                            .lastName("Zubov")
                            .build())
                    .build();

    final List<Account> EXPECTED_FIND_ALL =
            new ArrayList<>(asList(Account.builder()
                                    .accountId(1L)
                                    .accountNumber("4356 3245 1234 7345")
                                    .balance(new BigDecimal("245.30"))
                                    .owner(User.builder()
                                            .userId(5L)
                                            .firstName("Marsel")
                                            .lastName("Abramov")
                                            .build())
                                    .build(),
                    Account.builder()
                                    .accountId(2L)
                                    .accountNumber("4356 3834 1234 2855")
                                    .balance(new BigDecimal("30045.23"))
                                    .owner(User.builder()
                                            .userId(1L)
                                            .firstName("Andrey")
                                            .lastName("Sidorov")
                                            .build())
                                    .build(),
                    Account.builder()
                                    .accountId(3L)
                                    .accountNumber("7356 9264 7634 2534")
                                    .balance(new BigDecimal("2000.00"))
                                    .owner(User.builder()
                                            .userId(3L)
                                            .firstName("Anton")
                                            .lastName("Zubov")
                                            .build())
                                    .build(),
                    Account.builder()
                                    .accountId(4L)
                                    .accountNumber("2054 6334 2299 8376")
                                    .balance(new BigDecimal("7043.54"))
                                    .owner(User.builder()
                                            .userId(2L)
                                            .firstName("Sergey")
                                            .lastName("Larin")
                                            .build())
                                    .build(),
                    Account.builder()
                                    .accountId(5L)
                                    .accountNumber("9934 9264 3456 4423")
                                    .balance(new BigDecimal("12900.40"))
                                    .owner(User.builder()
                                            .userId(4L)
                                            .firstName("Zaur")
                                            .lastName("Ivanov")
                                            .build())
                                    .build()));

    final Account EXPECTED_SAVE =
            Account.builder()
                            .accountId(6L)
                            .accountNumber("9934 9264 3456 4423")
                            .balance(new BigDecimal("12900.40"))
                            .owner(User.builder()
                                    .userId(4L)
                                    .firstName("Zaur")
                                    .lastName("Ivanov")
                                    .build())
                            .build();

    final Account EXPECTED_UPDATE = Account.builder()
                    .accountId(4L)
                    .accountNumber("2353 6334 9865 8376")
                    .balance(new BigDecimal("1273.23"))
                    .owner(User.builder()
                            .userId(3L)
                            .firstName("Anton")
                            .lastName("Zubov")
                            .build())
                    .build();

    final List<Account> EXPECTED_DELETE =
            new ArrayList<>(asList(Account.builder()
                                    .accountId(1L)
                                    .accountNumber("4356 3245 1234 7345")
                                    .balance(new BigDecimal("245.30"))
                                    .owner(User.builder()
                                            .userId(5L)
                                            .firstName("Marsel")
                                            .lastName("Abramov")
                                            .build())
                                    .build(),
                    Account.builder()
                                    .accountId(3L)
                                    .accountNumber("7356 9264 7634 2534")
                                    .balance(new BigDecimal("2000.00"))
                                    .owner(User.builder()
                                            .userId(3L)
                                            .firstName("Anton")
                                            .lastName("Zubov")
                                            .build())
                                    .build(),
                    Account.builder()
                                    .accountId(4L)
                                    .accountNumber("2054 6334 2299 8376")
                                    .balance(new BigDecimal("7043.54"))
                                    .owner(User.builder()
                                            .userId(2L)
                                            .firstName("Sergey")
                                            .lastName("Larin")
                                            .build())
                                    .build(),
                    Account.builder()
                                    .accountId(5L)
                                    .accountNumber("9934 9264 3456 4423")
                                    .balance(new BigDecimal("12900.40"))
                                    .owner(User.builder()
                                            .userId(4L)
                                            .firstName("Zaur")
                                            .lastName("Ivanov")
                                            .build())
                                    .build()));

    @BeforeEach
    public void init() throws SQLException {
        new FullDatabase().fullDatabase();
        dataSource = ServiceDataSource.getDataSource();
    }

    @Test
    void isGet() {
        try {
            AccountRepositoryImp accountRepository = new AccountRepositoryImp(dataSource);
            Account check = accountRepository.get(3L).get();
            assertEquals(check, EXPECTED_GET_BY_ID);
        } catch (SQLException throwables) {
            fail();
            throwables.printStackTrace();
        }
    }

    @Test
    void isGetIsEmpty() {
        try {
            AccountRepositoryImp accountRepository = new AccountRepositoryImp(dataSource);
            Optional check = accountRepository.get(9L);
            assertEquals(check, Optional.empty());
        } catch (SQLException throwables) {
            fail();
            throwables.printStackTrace();
        }
    }

    @Test
    void isGetAll() {
        try {
            AccountRepositoryImp accountRepository = new AccountRepositoryImp(dataSource);
            List<Account> check = accountRepository.getAll();
            assertEquals(check, EXPECTED_FIND_ALL);
        } catch (SQLException throwables) {
            fail();
            throwables.printStackTrace();
        }
    }

    @Test
    void isSave() {
        try {
            AccountRepositoryImp accountRepository = new AccountRepositoryImp(dataSource);
            accountRepository.save(EXPECTED_SAVE);
            Account check = accountRepository.get(6L).get();
            assertEquals(check, EXPECTED_SAVE);
        } catch (SQLException throwables) {
            fail();
            throwables.printStackTrace();
        }
    }

    @Test
    void isUpdate() {
        try {
            AccountRepositoryImp accountRepository = new AccountRepositoryImp(dataSource);
            accountRepository.update(EXPECTED_UPDATE);
            Account check = accountRepository.get(4L).get();
            assertEquals(check, EXPECTED_UPDATE);
        } catch (SQLException throwables) {
            fail();
            throwables.printStackTrace();
        }
    }

    @Test
    void isDelete(){
        try {
            AccountRepositoryImp accountRepository = new AccountRepositoryImp(dataSource);
            accountRepository.delete(2L);
            List<Account> check = accountRepository.getAll();
            assertEquals(check, EXPECTED_DELETE);
        } catch (SQLException throwables) {
            fail();
            throwables.printStackTrace();
        }
    }

    @Test
    void isGetByNumber() {
        Account check = null;
        try {
            AccountRepositoryImp accountRepository = new AccountRepositoryImp(dataSource);
            check = accountRepository.getByNumber("7356 9264 7634 2534").get();
            assertEquals(check, EXPECTED_GET_BY_ID);
        } catch (SQLException throwables) {
            fail();
            throwables.printStackTrace();
        }
    }

    @Test
    void isGetByNumberIsEmpty(){
        try {
            AccountRepositoryImp accountRepository = new AccountRepositoryImp(dataSource);
            Optional check = accountRepository.getByNumber("7356 9264 7634 0000");
            assertEquals(check, Optional.empty());
        } catch (SQLException throwables) {
            fail();
            throwables.printStackTrace();
        }
    }
}
