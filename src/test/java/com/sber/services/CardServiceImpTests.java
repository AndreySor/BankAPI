package com.sber.services;

import com.sber.FullDatabase;
import com.sber.models.Account;
import com.sber.models.Card;
import com.sber.models.User;
import com.sber.repositories.CardRepositoryImp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CardServiceImpTests {

    @BeforeEach
    public void init() throws SQLException {
        new FullDatabase().fullDatabase();
    }

    @Test
    void isAddNewCardOnAccountNumber() {
        Account account = Account.builder()
                .accountNumber("4356 3834 1234 2855")
                .owner(User.builder()
                        .userId(1L)
                        .build())
                .build();

        CardServiceImp cardServiceImp = new CardServiceImp();
        String result = cardServiceImp.addNewCardOnAccountNumber(account);
        assertEquals(result, "SUCCESS");
    }

    @Test
    void isAddNewCardOnAccountNumberGenerator() {
        Account account = Account.builder()
                .accountNumber("4356 3834 1234 2855")
                .owner(User.builder()
                        .userId(1L)
                        .build())
                .build();

        CardServiceImp cardServiceImp = new CardServiceImp();
        cardServiceImp.addNewCardOnAccountNumber(account);
        CardRepositoryImp cardRepositoryImp = null;
        try {
            cardRepositoryImp = new CardRepositoryImp(ServiceDataSource.getDataSource());
            int result = cardRepositoryImp.get(6L).get().getCardNumber().length();
            assertEquals(result, 19);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Test
    void isAddNewCardOnAccountNumberIsAccountNull() {
        CardServiceImp cardServiceImp = new CardServiceImp();
        String result = cardServiceImp.addNewCardOnAccountNumber(null);
        assertEquals(result, "UNSUCCESS");
    }

    @Test
    void isAddNewCardOnAccountNumberIsAccountNumberNull() {
        Account account = Account.builder()
                .accountNumber(null)
                .build();

        CardServiceImp cardServiceImp = new CardServiceImp();
        String result = cardServiceImp.addNewCardOnAccountNumber(account);
        assertEquals(result, "UNSUCCESS");
    }

    @Test
    void isAddNewCardOnAccountNumberIsAccountNumberEmpty() {
        Account account = Account.builder()
                .accountNumber("")
                .build();

        CardServiceImp cardServiceImp = new CardServiceImp();
        String result = cardServiceImp.addNewCardOnAccountNumber(account);
        assertEquals(result, "UNSUCCESS");
    }

    @Test
    void isAddNewCardOnAccountNumberIsOwnerNull() {
        Account account = Account.builder()
                .accountNumber("4356 3834 1234 2855")
                .build();

        CardServiceImp cardServiceImp = new CardServiceImp();
        String result = cardServiceImp.addNewCardOnAccountNumber(account);
        assertEquals(result, "UNSUCCESS");
    }

    @Test
    void isAddNewCardOnAccountNumberIsOwnerIsWrong() {
        Account account = Account.builder()
                .accountNumber("4356 3834 1234 2855")
                .owner(User.builder()
                        .userId(3L)
                        .build())
                .build();

        CardServiceImp cardServiceImp = new CardServiceImp();
        String result = cardServiceImp.addNewCardOnAccountNumber(account);
        assertEquals(result, "UNSUCCESS");
    }
}
