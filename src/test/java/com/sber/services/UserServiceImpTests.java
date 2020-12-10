package com.sber.services;

import com.sber.models.Account;
import com.sber.models.Card;
import com.sber.models.User;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserServiceImpTests {

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
