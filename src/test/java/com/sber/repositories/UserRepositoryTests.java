package com.sber.repositories;

import com.sber.FullDatabase;
import com.sber.models.Account;
import com.sber.models.Card;
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

public class UserRepositoryTests {
    DataSource dataSource;

    final User EXPECTED_GET_BY_ID = User.builder()
            .userId(2L)
            .firstName("Sergey")
            .lastName("Larin")
            .accounts(new ArrayList<>(asList(Account.builder()
                    .accountId(4L)
                    .accountNumber("2054 6334 2299 8376")
                    .balance(new BigDecimal("7043.54"))
                    .build())))
            .cards(new ArrayList<>(asList(Card.builder()
                    .cardId(4L)
                    .cardNumber("2054 6334 2299 8376")
                    .account(Account.builder()
                            .accountId(4L)
                            .accountNumber("2054 6334 2299 8376")
                            .balance(new BigDecimal("7043.54"))
                            .build())
                    .build())))
            .build();

    final List<User> EXPECTED_GET_ALL = new ArrayList<>(asList(User.builder()
            .userId(1L)
            .firstName("Andrey")
            .lastName("Sidorov")
            .accounts(new ArrayList<>(asList(Account.builder()
                    .accountId(2L)
                    .accountNumber("4356 3834 1234 2855")
                    .balance(new BigDecimal("30045.23"))
                    .build())))
            .cards(new ArrayList<>(asList(Card.builder()
                    .cardId(2L)
                    .cardNumber("4356 3834 1234 2855")
                    .account(Account.builder()
                            .accountId(2L)
                            .accountNumber("4356 3834 1234 2855")
                            .balance(new BigDecimal("30045.23"))
                            .build())
                    .build())))
            .build(),
            User.builder()
                    .userId(2L)
                    .firstName("Sergey")
                    .lastName("Larin")
                    .accounts(new ArrayList<>(asList(Account.builder()
                            .accountId(4L)
                            .accountNumber("2054 6334 2299 8376")
                            .balance(new BigDecimal("7043.54"))
                            .build())))
                    .cards(new ArrayList<>(asList(Card.builder()
                            .cardId(4L)
                            .cardNumber("2054 6334 2299 8376")
                            .account(Account.builder()
                                    .accountId(4L)
                                    .accountNumber("2054 6334 2299 8376")
                                    .balance(new BigDecimal("7043.54"))
                                    .build())
                            .build())))
                    .build(),
            User.builder()
                    .userId(3L)
                    .firstName("Anton")
                    .lastName("Zubov")
                    .accounts(new ArrayList<>(asList(Account.builder()
                            .accountId(3L)
                            .accountNumber("7356 9264 7634 2534")
                            .balance(new BigDecimal("2000.00"))
                            .build())))
                    .cards(new ArrayList<>(asList(Card.builder()
                            .cardId(3L)
                            .cardNumber("7356 9264 7634 2534")
                            .account(Account.builder()
                                    .accountId(3L)
                                    .accountNumber("7356 9264 7634 2534")
                                    .balance(new BigDecimal("2000.00"))
                                    .build())
                            .build())))
                    .build(),
            User.builder()
                    .userId(4L)
                    .firstName("Zaur")
                    .lastName("Ivanov")
                    .accounts(new ArrayList<>(asList(Account.builder()
                            .accountId(5L)
                            .accountNumber("9934 9264 3456 4423")
                            .balance(new BigDecimal("12900.40"))
                            .build())))
                    .cards(new ArrayList<>(asList(Card.builder()
                            .cardId(5L)
                            .cardNumber("9934 9264 3456 4423")
                            .account(Account.builder()
                                    .accountId(5L)
                                    .accountNumber("9934 9264 3456 4423")
                                    .balance(new BigDecimal("12900.40"))
                                    .build())
                            .build())))
                    .build(),
            User.builder()
                    .userId(5L)
                    .firstName("Marsel")
                    .lastName("Abramov")
                    .accounts(new ArrayList<>(asList(Account.builder()
                            .accountId(1L)
                            .accountNumber("4356 3245 1234 7345")
                            .balance(new BigDecimal("245.30"))
                            .build())))
                    .cards(new ArrayList<>(asList(Card.builder()
                            .cardId(1L)
                            .cardNumber("4356 3245 1234 7345")
                            .account(Account.builder()
                                    .accountId(1L)
                                    .accountNumber("4356 3245 1234 7345")
                                    .balance(new BigDecimal("245.30"))
                                    .build())
                            .build())))
                    .build()));

    final User EXPECTED_SAVE = User.builder()
            .firstName("   ")
            .lastName("Pashin")
            .accounts(new ArrayList<>())
            .cards(new ArrayList<>())
            .build();

    final User EXPECTED_UPDATE = User.builder()
            .userId(2L)
            .firstName("Ivan")
            .lastName("Buzov")
            .accounts(new ArrayList<>(asList(Account.builder()
                    .accountId(4L)
                    .accountNumber("2054 6334 2299 8376")
                    .balance(new BigDecimal("7043.54"))
                    .build())))
            .cards(new ArrayList<>(asList(Card.builder()
                    .cardId(4L)
                    .cardNumber("2054 6334 2299 8376")
                    .account(Account.builder()
                            .accountId(4L)
                            .accountNumber("2054 6334 2299 8376")
                            .balance(new BigDecimal("7043.54"))
                            .build())
                    .build())))
            .build();

    final List<User> EXPECTED_DELETE = new ArrayList<>(asList(User.builder()
                    .userId(1L)
                    .firstName("Andrey")
                    .lastName("Sidorov")
                    .accounts(new ArrayList<>(asList(Account.builder()
                            .accountId(2L)
                            .accountNumber("4356 3834 1234 2855")
                            .balance(new BigDecimal("30045.23"))
                            .build())))
                    .cards(new ArrayList<>(asList(Card.builder()
                            .cardId(2L)
                            .cardNumber("4356 3834 1234 2855")
                            .account(Account.builder()
                                    .accountId(2L)
                                    .accountNumber("4356 3834 1234 2855")
                                    .balance(new BigDecimal("30045.23"))
                                    .build())
                            .build())))
                    .build(),
            User.builder()
                    .userId(3L)
                    .firstName("Anton")
                    .lastName("Zubov")
                    .accounts(new ArrayList<>(asList(Account.builder()
                            .accountId(3L)
                            .accountNumber("7356 9264 7634 2534")
                            .balance(new BigDecimal("2000.00"))
                            .build())))
                    .cards(new ArrayList<>(asList(Card.builder()
                            .cardId(3L)
                            .cardNumber("7356 9264 7634 2534")
                            .account(Account.builder()
                                    .accountId(3L)
                                    .accountNumber("7356 9264 7634 2534")
                                    .balance(new BigDecimal("2000.00"))
                                    .build())
                            .build())))
                    .build(),
            User.builder()
                    .userId(4L)
                    .firstName("Zaur")
                    .lastName("Ivanov")
                    .accounts(new ArrayList<>(asList(Account.builder()
                            .accountId(5L)
                            .accountNumber("9934 9264 3456 4423")
                            .balance(new BigDecimal("12900.40"))
                            .build())))
                    .cards(new ArrayList<>(asList(Card.builder()
                            .cardId(5L)
                            .cardNumber("9934 9264 3456 4423")
                            .account(Account.builder()
                                    .accountId(5L)
                                    .accountNumber("9934 9264 3456 4423")
                                    .balance(new BigDecimal("12900.40"))
                                    .build())
                            .build())))
                    .build(),
            User.builder()
                    .userId(5L)
                    .firstName("Marsel")
                    .lastName("Abramov")
                    .accounts(new ArrayList<>(asList(Account.builder()
                            .accountId(1L)
                            .accountNumber("4356 3245 1234 7345")
                            .balance(new BigDecimal("245.30"))
                            .build())))
                    .cards(new ArrayList<>(asList(Card.builder()
                            .cardId(1L)
                            .cardNumber("4356 3245 1234 7345")
                            .account(Account.builder()
                                    .accountId(1L)
                                    .accountNumber("4356 3245 1234 7345")
                                    .balance(new BigDecimal("245.30"))
                                    .build())
                            .build())))
                    .build()));

    @BeforeEach
    public void init() throws SQLException {
        new FullDatabase().fullDatabase();
        dataSource = ServiceDataSource.getDataSource();
    }

    @Test
    void isGet() {
        try {
            UserRepositoryImp userRepository = new UserRepositoryImp(dataSource);
            User  check = userRepository.get(2L).get();
            assertEquals(check, EXPECTED_GET_BY_ID);
        } catch (SQLException throwables) {
            fail();
            throwables.printStackTrace();
        }
    }

    @Test
    void isGetAll() {
        try {
            UserRepositoryImp userRepository = new UserRepositoryImp(dataSource);
            List<User> check = userRepository.getAll();
            assertEquals(check, EXPECTED_GET_ALL);
        } catch (SQLException throwables) {
            fail();
            throwables.printStackTrace();
        }
    }

    @Test
    void isSave() {
        try {
            UserRepositoryImp userRepository = new UserRepositoryImp(dataSource);
            userRepository.save(EXPECTED_SAVE);
            User check = userRepository.get(EXPECTED_SAVE.getUserId()).get();
            assertEquals(check, EXPECTED_SAVE);
        } catch (SQLException throwables) {
            fail();
            throwables.printStackTrace();
        }
    }

    @Test
    void isUpdate() {
        try {
            UserRepositoryImp userRepository = new UserRepositoryImp(dataSource);
            userRepository.update(EXPECTED_UPDATE);
            User check = userRepository.get(2L).get();
            assertEquals(check, EXPECTED_UPDATE);
        } catch (SQLException throwables) {
            fail();
            throwables.printStackTrace();
        }
    }

    @Test
    void isDelete(){
        try {
            UserRepositoryImp userRepository = new UserRepositoryImp(dataSource);
            userRepository.delete(2L);
            List<User> check = userRepository.getAll();
            assertEquals(check, EXPECTED_DELETE);
        } catch (SQLException throwables) {
            fail();
            throwables.printStackTrace();
        }
    }

    @Test
    void isGetEmpty() {
        try {
            UserRepositoryImp userRepository = new UserRepositoryImp(dataSource);
            Optional check = userRepository.get(9L);
            assertEquals(check, Optional.empty());
        } catch (SQLException throwables) {
            fail();
            throwables.printStackTrace();
        }
    }
}
