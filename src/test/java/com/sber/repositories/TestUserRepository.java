package com.sber.repositories;

import com.sber.models.Account;
import com.sber.models.Card;
import com.sber.models.User;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestUserRepository {
    DataSource dataSource;

    final User EXPECTED_GET_BY_ID = User.builder()
            .userId(2l)
            .firstName("Sergey")
            .lastName("Larin")
            .accounts(new ArrayList<>(asList(Account.builder()
                    .accountId(4l)
                    .accountNumber("2054 6334 2299 8376")
                    .balance(new BigDecimal("7043.54"))
                    .build())))
            .cards(new ArrayList<>(asList(Card.builder()
                    .cardId(4l)
                    .cardNumber("2054 6334 2299 8376")
                    .account(Account.builder()
                            .accountId(4l)
                            .accountNumber("2054 6334 2299 8376")
                            .balance(new BigDecimal("7043.54"))
                            .build())
                    .build())))
            .build();

    final List<User> EXPECTET_GET_ALL = new ArrayList<>(asList(User.builder()
            .userId(1l)
            .firstName("Andrey")
            .lastName("Sidorov")
            .accounts(new ArrayList<>(asList(Account.builder()
                    .accountId(2l)
                    .accountNumber("4356 3834 1234 2855")
                    .balance(new BigDecimal("30045.23"))
                    .build())))
            .cards(new ArrayList<>(asList(Card.builder()
                    .cardId(2l)
                    .cardNumber("4356 3834 1234 2855")
                    .account(Account.builder()
                            .accountId(2l)
                            .accountNumber("4356 3834 1234 2855")
                            .balance(new BigDecimal("30045.23"))
                            .build())
                    .build())))
            .build(),
            User.builder()
                    .userId(2l)
                    .firstName("Sergey")
                    .lastName("Larin")
                    .accounts(new ArrayList<>(asList(Account.builder()
                            .accountId(4l)
                            .accountNumber("2054 6334 2299 8376")
                            .balance(new BigDecimal("7043.54"))
                            .build())))
                    .cards(new ArrayList<>(asList(Card.builder()
                            .cardId(4l)
                            .cardNumber("2054 6334 2299 8376")
                            .account(Account.builder()
                                    .accountId(4l)
                                    .accountNumber("2054 6334 2299 8376")
                                    .balance(new BigDecimal("7043.54"))
                                    .build())
                            .build())))
                    .build(),
            User.builder()
                    .userId(3l)
                    .firstName("Anton")
                    .lastName("Zubov")
                    .accounts(new ArrayList<>(asList(Account.builder()
                            .accountId(3l)
                            .accountNumber("7356 9264 7634 2534")
                            .balance(new BigDecimal("2000.00"))
                            .build())))
                    .cards(new ArrayList<>(asList(Card.builder()
                            .cardId(3l)
                            .cardNumber("7356 9264 7634 2534")
                            .account(Account.builder()
                                    .accountId(3l)
                                    .accountNumber("7356 9264 7634 2534")
                                    .balance(new BigDecimal("2000.00"))
                                    .build())
                            .build())))
                    .build(),
            User.builder()
                    .userId(4l)
                    .firstName("Zaur")
                    .lastName("Ivanov")
                    .accounts(new ArrayList<>(asList(Account.builder()
                            .accountId(5l)
                            .accountNumber("9934 9264 3456 4423")
                            .balance(new BigDecimal("12900.40"))
                            .build())))
                    .cards(new ArrayList<>(asList(Card.builder()
                            .cardId(5l)
                            .cardNumber("9934 9264 3456 4423")
                            .account(Account.builder()
                                    .accountId(5l)
                                    .accountNumber("9934 9264 3456 4423")
                                    .balance(new BigDecimal("12900.40"))
                                    .build())
                            .build())))
                    .build(),
            User.builder()
                    .userId(5l)
                    .firstName("Marsel")
                    .lastName("Abramov")
                    .accounts(new ArrayList<>(asList(Account.builder()
                            .accountId(1l)
                            .accountNumber("4356 3245 1234 7345")
                            .balance(new BigDecimal("245.30"))
                            .build())))
                    .cards(new ArrayList<>(asList(Card.builder()
                            .cardId(1l)
                            .cardNumber("4356 3245 1234 7345")
                            .account(Account.builder()
                                    .accountId(1l)
                                    .accountNumber("4356 3245 1234 7345")
                                    .balance(new BigDecimal("245.30"))
                                    .build())
                            .build())))
                    .build()));

    @BeforeEach
    public void init() {
        JdbcDataSource ds = new JdbcDataSource();
        ds.setUser("sa");
        ds.setPassword("");
        ds.setUrl("jdbc:h2:~/test;AUTO_SERVER=TRUE;Mode=Oracle;INIT=runscript from 'src/main/resources/schema.sql'\\;runscript from 'src/main/resources/data.sql'");
        dataSource = ds;
    }

    @Test
    void isGet() {
        UserRepositoryImp userRepository = new UserRepositoryImp(dataSource);
        User check = userRepository.get(2l).get();
        assertEquals(check, EXPECTED_GET_BY_ID);
    }

    @Test
    void isGetAll() {
        UserRepositoryImp userRepository = new UserRepositoryImp(dataSource);
        List<User> check = userRepository.getAll();
        assertEquals(check, EXPECTET_GET_ALL);
    }
}
