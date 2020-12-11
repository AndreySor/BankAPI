package com.sber.services;

import com.sber.models.Account;
import com.sber.models.Card;
import com.sber.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserServiceImpTests {

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

    final List<Card> EXPECTED_FIND_CARDS = new ArrayList<>(asList(Card.builder()
            .cardId(4L)
            .cardNumber("2054 6334 2299 8376")
            .account(Account.builder()
                    .accountId(4L)
                    .accountNumber("2054 6334 2299 8376")
                    .balance(new BigDecimal("7043.54"))
                    .build())
            .build()));

    @Test
    void isReturnListCards() {
        User user = User.builder()
                .userId(2L)
                .firstName("Sergey")
                .lastName("Larin")
                .build();
        UserServiceImp userServiceImp = new UserServiceImp();
        List<Card> cards = userServiceImp.returnListCards(user);
        assertEquals(cards, EXPECTED_FIND_CARDS);
    }

    @Test
    void isReturnListCardsIsUserIsNull() {
        User user = User.builder()
                .userId(null)
                .firstName("Sergey")
                .lastName("Larin")
                .build();
        UserServiceImp userServiceImp = new UserServiceImp();
        List<Card> cards = userServiceImp.returnListCards(user);
        assertEquals(cards, new ArrayList<>());
    }

    @Test
    void isReturnListCardsIsUserIsZero() {
        User user = User.builder()
                .userId(0L)
                .firstName("Sergey")
                .lastName("Larin")
                .build();
        UserServiceImp userServiceImp = new UserServiceImp();
        List<Card> cards = userServiceImp.returnListCards(user);
        assertEquals(cards, new ArrayList<>());
    }
}
