package com.sber.repositories;

import com.sber.models.Account;
import com.sber.models.Card;
import com.sber.models.User;
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

public class CardRepositoryTests {
    DataSource dataSource;

    final Card EXPECTED_GET_BY_ID = Card.builder()
                                        .cardId(3L)
                                        .cardNumber("7356 9264 7634 2534")
                                        .account(Account.builder()
                                                .accountId(3L)
                                                .accountNumber("7356 9264 7634 2534")
                                                .balance(new BigDecimal("2000.00"))
                                                .owner(User.builder()
                                                        .userId(3L)
                                                        .firstName("Anton")
                                                        .lastName("Zubov")
                                                        .build())
                                                .build())
                                        .owner(User.builder()
                                                .userId(3L)
                                                .firstName("Anton")
                                                .lastName("Zubov")
                                                .build())
                                        .build();

    final List<Card> EXPECTED_FIND_ALL =
            new ArrayList<>(asList(Card.builder()
                    .cardId(1L)
                    .cardNumber("4356 3245 1234 7345")
                    .account(Account.builder()
                            .accountId(1L)
                            .accountNumber("4356 3245 1234 7345")
                            .balance(new BigDecimal("245.30"))
                            .owner(User.builder()
                                    .userId(5L)
                                    .firstName("Marsel")
                                    .lastName("Abramov")
                                    .build())
                            .build())
                    .owner(User.builder()
                            .userId(5L)
                            .firstName("Marsel")
                            .lastName("Abramov")
                            .build())
                    .build(),
                    Card.builder()
                            .cardId(2L)
                            .cardNumber("4356 3834 1234 2855")
                            .account(Account.builder()
                                    .accountId(2L)
                                    .accountNumber("4356 3834 1234 2855")
                                    .balance(new BigDecimal("30045.23"))
                                    .owner(User.builder()
                                            .userId(1L)
                                            .firstName("Andrey")
                                            .lastName("Sidorov")
                                            .build())
                                    .build())
                            .owner(User.builder()
                                    .userId(1L)
                                    .firstName("Andrey")
                                    .lastName("Sidorov")
                                    .build())
                            .build(),
                    Card.builder()
                            .cardId(3L)
                            .cardNumber("7356 9264 7634 2534")
                            .account(Account.builder()
                                    .accountId(3L)
                                    .accountNumber("7356 9264 7634 2534")
                                    .balance(new BigDecimal("2000.00"))
                                    .owner(User.builder()
                                            .userId(3L)
                                            .firstName("Anton")
                                            .lastName("Zubov")
                                            .build())
                                    .build())
                            .owner(User.builder()
                                    .userId(3L)
                                    .firstName("Anton")
                                    .lastName("Zubov")
                                    .build())
                            .build(),
                    Card.builder()
                            .cardId(4L)
                            .cardNumber("2054 6334 2299 8376")
                            .account(Account.builder()
                                    .accountId(4L)
                                    .accountNumber("2054 6334 2299 8376")
                                    .balance(new BigDecimal("7043.54"))
                                    .owner(User.builder()
                                            .userId(2L)
                                            .firstName("Sergey")
                                            .lastName("Larin")
                                            .build())
                                    .build())
                            .owner(User.builder()
                                    .userId(2L)
                                    .firstName("Sergey")
                                    .lastName("Larin")
                                    .build())
                            .build(),
                    Card.builder()
                            .cardId(5L)
                            .cardNumber("9934 9264 3456 4423")
                            .account(Account.builder()
                                    .accountId(5L)
                                    .accountNumber("9934 9264 3456 4423")
                                    .balance(new BigDecimal("12900.40"))
                                    .owner(User.builder()
                                            .userId(4L)
                                            .firstName("Zaur")
                                            .lastName("Ivanov")
                                            .build())
                                    .build())
                            .owner(User.builder()
                                    .userId(4L)
                                    .firstName("Zaur")
                                    .lastName("Ivanov")
                                    .build())
                            .build()));

    final Card EXPECTED_SAVE =
                    Card.builder()
                            .cardNumber("9934 2323 3896 6623")
                            .account(Account.builder()
                                    .accountId(5L)
                                    .accountNumber("9934 9264 3456 4423")
                                    .balance(new BigDecimal("12900.40"))
                                    .owner(User.builder()
                                            .userId(4L)
                                            .firstName("Zaur")
                                            .lastName("Ivanov")
                                            .build())
                                    .build())
                            .owner(User.builder()
                                    .userId(4L)
                                    .firstName("Zaur")
                                    .lastName("Ivanov")
                                    .build())
                            .build();

    final Card EXPECTED_UPDATE = Card.builder()
            .cardId(3L)
            .cardNumber("7356 6385 7634 2534")
            .account(Account.builder()
                    .accountId(4L)
                    .accountNumber("2054 6334 2299 8376")
                    .balance(new BigDecimal("7043.54"))
                    .owner(User.builder()
                            .userId(2L)
                            .firstName("Sergey")
                            .lastName("Larin")
                            .build())
                    .build())
            .owner(User.builder()
                    .userId(2L)
                    .firstName("Sergey")
                    .lastName("Larin")
                    .build())
            .build();

    final List<Card> EXPECTED_DELETE =
            new ArrayList<>(asList(Card.builder()
                            .cardId(1L)
                            .cardNumber("4356 3245 1234 7345")
                            .account(Account.builder()
                                    .accountId(1L)
                                    .accountNumber("4356 3245 1234 7345")
                                    .balance(new BigDecimal("245.30"))
                                    .owner(User.builder()
                                            .userId(5L)
                                            .firstName("Marsel")
                                            .lastName("Abramov")
                                            .build())
                                    .build())
                            .owner(User.builder()
                                    .userId(5L)
                                    .firstName("Marsel")
                                    .lastName("Abramov")
                                    .build())
                            .build(),
                    Card.builder()
                            .cardId(3L)
                            .cardNumber("7356 9264 7634 2534")
                            .account(Account.builder()
                                    .accountId(3L)
                                    .accountNumber("7356 9264 7634 2534")
                                    .balance(new BigDecimal("2000.00"))
                                    .owner(User.builder()
                                            .userId(3L)
                                            .firstName("Anton")
                                            .lastName("Zubov")
                                            .build())
                                    .build())
                            .owner(User.builder()
                                    .userId(3L)
                                    .firstName("Anton")
                                    .lastName("Zubov")
                                    .build())
                            .build(),
                    Card.builder()
                            .cardId(4L)
                            .cardNumber("2054 6334 2299 8376")
                            .account(Account.builder()
                                    .accountId(4L)
                                    .accountNumber("2054 6334 2299 8376")
                                    .balance(new BigDecimal("7043.54"))
                                    .owner(User.builder()
                                            .userId(2L)
                                            .firstName("Sergey")
                                            .lastName("Larin")
                                            .build())
                                    .build())
                            .owner(User.builder()
                                    .userId(2L)
                                    .firstName("Sergey")
                                    .lastName("Larin")
                                    .build())
                            .build(),
                    Card.builder()
                            .cardId(5L)
                            .cardNumber("9934 9264 3456 4423")
                            .account(Account.builder()
                                    .accountId(5L)
                                    .accountNumber("9934 9264 3456 4423")
                                    .balance(new BigDecimal("12900.40"))
                                    .owner(User.builder()
                                            .userId(4L)
                                            .firstName("Zaur")
                                            .lastName("Ivanov")
                                            .build())
                                    .build())
                            .owner(User.builder()
                                    .userId(4L)
                                    .firstName("Zaur")
                                    .lastName("Ivanov")
                                    .build())
                            .build()));

    @BeforeEach
    public void init() throws SQLException {
        JdbcDataSource ds = new JdbcDataSource();
        ds.setUser("sa");
        ds.setPassword("");
        ds.setUrl("jdbc:h2:~/test");
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
        dataSource = ds;
    }

    @Test
    void isGet() {
        try {
            CardRepositoryImp cardRepository = new CardRepositoryImp(dataSource);
            Card check = cardRepository.get(3L).get();
            assertEquals(check, EXPECTED_GET_BY_ID);
        } catch (SQLException throwables) {
            fail();
            throwables.printStackTrace();
        }
    }

    @Test
    void isGetEmpty() {
        try {
            CardRepositoryImp cardRepository = new CardRepositoryImp(dataSource);
            Optional check = cardRepository.get(9L);
            assertEquals(check, Optional.empty());
        } catch (SQLException throwables) {
            fail();
            throwables.printStackTrace();
        }
    }

    @Test
    void isGetAll() {
        try {
            CardRepositoryImp cardRepository = new CardRepositoryImp(dataSource);
            List<Card> check = cardRepository.getAll();
            assertEquals(check, EXPECTED_FIND_ALL);
        } catch (SQLException throwables) {
            fail();
            throwables.printStackTrace();
        }
    }

    @Test
    void isSave() {
        try {
            CardRepositoryImp cardRepository = new CardRepositoryImp(dataSource);
            cardRepository.save(EXPECTED_SAVE);
            Card check = cardRepository.get(6L).get();
            assertEquals(check, EXPECTED_SAVE);
        } catch (SQLException throwables) {
            fail();
            throwables.printStackTrace();
        }
    }

    @Test
    void isUpdate() {
        try {
            CardRepositoryImp cardRepository = new CardRepositoryImp(dataSource);
            cardRepository.update(EXPECTED_UPDATE);
            Card check = cardRepository.get(3L).get();
            assertEquals(check, EXPECTED_UPDATE);
        } catch (SQLException throwables) {
            fail();
            throwables.printStackTrace();
        }
    }

    @Test
    void isDelete() {
        List<Card> check = null;
        try {
            CardRepositoryImp cardRepository = new CardRepositoryImp(dataSource);
            cardRepository.delete(2L);
            check = cardRepository.getAll();
            assertEquals(check, EXPECTED_DELETE);
        } catch (SQLException throwables) {
            fail();
            throwables.printStackTrace();
        }
    }

    @Test
    void isGetByNumber() {
        try {
            CardRepositoryImp cardRepository = new CardRepositoryImp(dataSource);
            Card check = cardRepository.getByNumber("7356 9264 7634 2534").get();
            assertEquals(check, EXPECTED_GET_BY_ID);
        } catch (SQLException throwables) {
            fail();
            throwables.printStackTrace();
        }
    }

    @Test
    void isGetByNumberIsEmpty() {
        try {
            CardRepositoryImp cardRepository = new CardRepositoryImp(dataSource);
            Optional check = cardRepository.getByNumber("7356 9264 7634 0000");
            assertEquals(check, Optional.empty());
        } catch (SQLException throwables) {
            fail();
            throwables.printStackTrace();
        }
    }
}
