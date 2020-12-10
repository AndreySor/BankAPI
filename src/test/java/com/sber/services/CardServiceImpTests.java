package com.sber.services;

import com.sber.models.Account;
import com.sber.repositories.CardRepositoryImp;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CardServiceImpTests {

    @Test
    void isAddNewCardOnAccountNumber() {
        Account account = Account.builder()
                .accountNumber("4356 3834 1234 2855")
                .build();

        CardServiceImp cardServiceImp = new CardServiceImp();
        String result = cardServiceImp.addNewCardOnAccountNumber(account);
        assertEquals(result, "SUCCESS");
    }

    @Test
    void isAddNewCardOnAccountNumberGenerator() {
        Account account = Account.builder()
                .accountNumber("4356 3834 1234 2855")
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
}
