package com.sber.repositories;

import com.sber.models.Account;
import com.sber.models.Card;
import com.sber.models.User;
import org.h2.jdbcx.JdbcDataSource;
import org.h2.tools.DeleteDbFiles;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.*;

public class TestsCardRepository {
    DataSource dataSource;

    final Card EXPECTED_GET_BY_ID = Card.builder()
                                        .cardId(3l)
                                        .cardNumber("7356 9264 7634 2534")
                                        .account(Account.builder()
                                                .accountId(3l)
                                                .accountNumber("7356 9264 7634 2534")
                                                .balance(new BigDecimal("2000.00"))
                                                .owner(User.builder()
                                                        .userId(3l)
                                                        .firstName("Anton")
                                                        .lastName("Zubov")
                                                        .build())
                                                .build())
                                        .owner(User.builder()
                                                .userId(3l)
                                                .firstName("Anton")
                                                .lastName("Zubov")
                                                .build())
                                        .build();

    final List<Card> EXPECTED_FIND_ALL =
            new ArrayList<>(asList(Card.builder()
                    .cardId(1l)
                    .cardNumber("4356 3245 1234 7345")
                    .account(Account.builder()
                            .accountId(1l)
                            .accountNumber("4356 3245 1234 7345")
                            .balance(new BigDecimal("245.30"))
                            .owner(User.builder()
                                    .userId(5l)
                                    .firstName("Marsel")
                                    .lastName("Abramov")
                                    .build())
                            .build())
                    .owner(User.builder()
                            .userId(5l)
                            .firstName("Marsel")
                            .lastName("Abramov")
                            .build())
                    .build(),
                    Card.builder()
                            .cardId(2l)
                            .cardNumber("4356 3834 1234 2855")
                            .account(Account.builder()
                                    .accountId(2l)
                                    .accountNumber("4356 3834 1234 2855")
                                    .balance(new BigDecimal("30045.23"))
                                    .owner(User.builder()
                                            .userId(1l)
                                            .firstName("Andrey")
                                            .lastName("Sidorov")
                                            .build())
                                    .build())
                            .owner(User.builder()
                                    .userId(1l)
                                    .firstName("Andrey")
                                    .lastName("Sidorov")
                                    .build())
                            .build(),
                    Card.builder()
                            .cardId(3l)
                            .cardNumber("7356 9264 7634 2534")
                            .account(Account.builder()
                                    .accountId(3l)
                                    .accountNumber("7356 9264 7634 2534")
                                    .balance(new BigDecimal("2000.00"))
                                    .owner(User.builder()
                                            .userId(3l)
                                            .firstName("Anton")
                                            .lastName("Zubov")
                                            .build())
                                    .build())
                            .owner(User.builder()
                                    .userId(3l)
                                    .firstName("Anton")
                                    .lastName("Zubov")
                                    .build())
                            .build(),
                    Card.builder()
                            .cardId(4l)
                            .cardNumber("2054 6334 2299 8376")
                            .account(Account.builder()
                                    .accountId(4l)
                                    .accountNumber("2054 6334 2299 8376")
                                    .balance(new BigDecimal("7043.54"))
                                    .owner(User.builder()
                                            .userId(2l)
                                            .firstName("Sergey")
                                            .lastName("Larin")
                                            .build())
                                    .build())
                            .owner(User.builder()
                                    .userId(2l)
                                    .firstName("Sergey")
                                    .lastName("Larin")
                                    .build())
                            .build(),
                    Card.builder()
                            .cardId(5l)
                            .cardNumber("9934 9264 3456 4423")
                            .account(Account.builder()
                                    .accountId(5l)
                                    .accountNumber("9934 9264 3456 4423")
                                    .balance(new BigDecimal("12900.40"))
                                    .owner(User.builder()
                                            .userId(4l)
                                            .firstName("Zaur")
                                            .lastName("Ivanov")
                                            .build())
                                    .build())
                            .owner(User.builder()
                                    .userId(4l)
                                    .firstName("Zaur")
                                    .lastName("Ivanov")
                                    .build())
                            .build()));

    final Card EXPECTED_SAVE =
                    Card.builder()
                            .cardId(6l)
                            .cardNumber("9934 2323 3896 6623")
                            .account(Account.builder()
                                    .accountId(5l)
                                    .accountNumber("9934 9264 3456 4423")
                                    .balance(new BigDecimal("12900.40"))
                                    .owner(User.builder()
                                            .userId(4l)
                                            .firstName("Zaur")
                                            .lastName("Ivanov")
                                            .build())
                                    .build())
                            .owner(User.builder()
                                    .userId(4l)
                                    .firstName("Zaur")
                                    .lastName("Ivanov")
                                    .build())
                            .build();

    final Card EXPECTED_SAVE_NOT_ACCOUNT_ID =
            Card.builder()
                    .cardId(6l)
                    .cardNumber("9934 2323 3896 6623")
                    .account(Account.builder()
                            .accountId(7l)
                            .accountNumber("9934 9264 3456 4423")
                            .balance(new BigDecimal("12900.40"))
                            .owner(User.builder()
                                    .userId(4l)
                                    .firstName("Zaur")
                                    .lastName("Ivanov")
                                    .build())
                            .build())
                    .owner(User.builder()
                            .userId(4l)
                            .firstName("Zaur")
                            .lastName("Ivanov")
                            .build())
                    .build();

    final Card EXPECTED_SAVE_NOT_OWNER_ID =
            Card.builder()
                    .cardId(6l)
                    .cardNumber("9934 2323 3896 6623")
                    .account(Account.builder()
                            .accountId(5l)
                            .accountNumber("9934 9264 3456 4423")
                            .balance(new BigDecimal("12900.40"))
                            .owner(User.builder()
                                    .userId(4l)
                                    .firstName("Zaur")
                                    .lastName("Ivanov")
                                    .build())
                            .build())
                    .owner(User.builder()
                            .userId(8l)
                            .firstName("Zaur")
                            .lastName("Ivanov")
                            .build())
                    .build();

    final Card EXPECTED_SAVE_ACCOUNT_ID_IS_NULL =
            Card.builder()
                    .cardId(6l)
                    .cardNumber("9934 2323 3896 6623")
                    .account(Account.builder()
                            .accountId(null)
                            .accountNumber("9934 9264 3456 4423")
                            .balance(new BigDecimal("12900.40"))
                            .owner(User.builder()
                                    .userId(4l)
                                    .firstName("Zaur")
                                    .lastName("Ivanov")
                                    .build())
                            .build())
                    .owner(User.builder()
                            .userId(4l)
                            .firstName("Zaur")
                            .lastName("Ivanov")
                            .build())
                    .build();

    final Card EXPECTED_SAVE_OWNER_ID_IS_NULL =
            Card.builder()
                    .cardId(6l)
                    .cardNumber("9934 2323 3896 6623")
                    .account(Account.builder()
                            .accountId(5l)
                            .accountNumber("9934 9264 3456 4423")
                            .balance(new BigDecimal("12900.40"))
                            .owner(User.builder()
                                    .userId(4l)
                                    .firstName("Zaur")
                                    .lastName("Ivanov")
                                    .build())
                            .build())
                    .owner(User.builder()
                            .userId(null)
                            .firstName("Zaur")
                            .lastName("Ivanov")
                            .build())
                    .build();

    final Card EXPECTED_UPDATE = Card.builder()
            .cardId(3l)
            .cardNumber("7356 6385 7634 2534")
            .account(Account.builder()
                    .accountId(4l)
                    .accountNumber("2054 6334 2299 8376")
                    .balance(new BigDecimal("7043.54"))
                    .owner(User.builder()
                            .userId(2l)
                            .firstName("Sergey")
                            .lastName("Larin")
                            .build())
                    .build())
            .owner(User.builder()
                    .userId(2l)
                    .firstName("Sergey")
                    .lastName("Larin")
                    .build())
            .build();

    final Card EXPECTED_UPDATE_CARD_NUMBER_IS_NULL = Card.builder()
            .cardId(3l)
            .cardNumber(null)
            .account(Account.builder()
                    .accountId(4l)
                    .accountNumber("2054 6334 2299 8376")
                    .balance(new BigDecimal("7043.54"))
                    .owner(User.builder()
                            .userId(2l)
                            .firstName("Sergey")
                            .lastName("Larin")
                            .build())
                    .build())
            .owner(User.builder()
                    .userId(2l)
                    .firstName("Sergey")
                    .lastName("Larin")
                    .build())
            .build();

    final Card EXPECTED_UPDATE_USER_ID_IS_NULL = Card.builder()
            .cardId(3l)
            .cardNumber("7356 6385 7634 2534")
            .account(Account.builder()
                    .accountId(4l)
                    .accountNumber("2054 6334 2299 8376")
                    .balance(new BigDecimal("7043.54"))
                    .owner(User.builder()
                            .userId(2l)
                            .firstName("Sergey")
                            .lastName("Larin")
                            .build())
                    .build())
            .owner(User.builder()
                    .userId(null)
                    .firstName("Sergey")
                    .lastName("Larin")
                    .build())
            .build();

    final Card EXPECTED_UPDATE_ACCOUNT_ID_IS_NULL = Card.builder()
            .cardId(3l)
            .cardNumber("7356 6385 7634 2534")
            .account(Account.builder()
                    .accountId(null)
                    .accountNumber("2054 6334 2299 8376")
                    .balance(new BigDecimal("7043.54"))
                    .owner(User.builder()
                            .userId(2l)
                            .firstName("Sergey")
                            .lastName("Larin")
                            .build())
                    .build())
            .owner(User.builder()
                    .userId(2l)
                    .firstName("Sergey")
                    .lastName("Larin")
                    .build())
            .build();

    final List<Card> EXPECTED_DELETE =
            new ArrayList<>(asList(Card.builder()
                            .cardId(1l)
                            .cardNumber("4356 3245 1234 7345")
                            .account(Account.builder()
                                    .accountId(1l)
                                    .accountNumber("4356 3245 1234 7345")
                                    .balance(new BigDecimal("245.30"))
                                    .owner(User.builder()
                                            .userId(5l)
                                            .firstName("Marsel")
                                            .lastName("Abramov")
                                            .build())
                                    .build())
                            .owner(User.builder()
                                    .userId(5l)
                                    .firstName("Marsel")
                                    .lastName("Abramov")
                                    .build())
                            .build(),
                    Card.builder()
                            .cardId(3l)
                            .cardNumber("7356 9264 7634 2534")
                            .account(Account.builder()
                                    .accountId(3l)
                                    .accountNumber("7356 9264 7634 2534")
                                    .balance(new BigDecimal("2000.00"))
                                    .owner(User.builder()
                                            .userId(3l)
                                            .firstName("Anton")
                                            .lastName("Zubov")
                                            .build())
                                    .build())
                            .owner(User.builder()
                                    .userId(3l)
                                    .firstName("Anton")
                                    .lastName("Zubov")
                                    .build())
                            .build(),
                    Card.builder()
                            .cardId(4l)
                            .cardNumber("2054 6334 2299 8376")
                            .account(Account.builder()
                                    .accountId(4l)
                                    .accountNumber("2054 6334 2299 8376")
                                    .balance(new BigDecimal("7043.54"))
                                    .owner(User.builder()
                                            .userId(2l)
                                            .firstName("Sergey")
                                            .lastName("Larin")
                                            .build())
                                    .build())
                            .owner(User.builder()
                                    .userId(2l)
                                    .firstName("Sergey")
                                    .lastName("Larin")
                                    .build())
                            .build(),
                    Card.builder()
                            .cardId(5l)
                            .cardNumber("9934 9264 3456 4423")
                            .account(Account.builder()
                                    .accountId(5l)
                                    .accountNumber("9934 9264 3456 4423")
                                    .balance(new BigDecimal("12900.40"))
                                    .owner(User.builder()
                                            .userId(4l)
                                            .firstName("Zaur")
                                            .lastName("Ivanov")
                                            .build())
                                    .build())
                            .owner(User.builder()
                                    .userId(4l)
                                    .firstName("Zaur")
                                    .lastName("Ivanov")
                                    .build())
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
        try {
            CardRepositoryImp cardRepository = new CardRepositoryImp(dataSource);
            Card check = cardRepository.get(3l).get();
            assertEquals(check, EXPECTED_GET_BY_ID);
        } catch (SQLException throwables) {
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
            throwables.printStackTrace();
        }
    }

    @Test
    void isSave() {
        try {
            CardRepositoryImp cardRepository = new CardRepositoryImp(dataSource);
            cardRepository.save(EXPECTED_SAVE);
            Card check = cardRepository.get(6l).get();
            assertEquals(check, EXPECTED_SAVE);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Test
    void isSaveNotOwnerId() {
        try {
            CardRepositoryImp cardRepository = new CardRepositoryImp(dataSource);
            Exception exception = assertThrows(NotSavedSubEntityException.class, () -> {
                cardRepository.save(EXPECTED_SAVE_NOT_OWNER_ID);
            });
            String expectedMessage = "В таблице sber_users не существует записи с id =  8";
            String actualMessage = exception.getMessage();
            assertTrue(actualMessage.contains(expectedMessage));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    @Test
    void isSaveNotAccountId() {
        try {
            CardRepositoryImp cardRepository = new CardRepositoryImp(dataSource);
            Exception exception = assertThrows(NotSavedSubEntityException.class, () -> {
                cardRepository.save(EXPECTED_SAVE_NOT_ACCOUNT_ID);
            });
            String expectedMessage = "В таблице sber_accounts не существует записи с id =  7";
            String actualMessage = exception.getMessage();
            assertTrue(actualMessage.contains(expectedMessage));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    @Test
    void isSaveNotOwnerIdIsNull() {
        try {
            CardRepositoryImp cardRepository = new CardRepositoryImp(dataSource);
            Exception exception = assertThrows(NotSavedSubEntityException.class, () -> {
                cardRepository.save(EXPECTED_SAVE_OWNER_ID_IS_NULL);
            });
            String expectedMessage = "Значение userId =  null";
            String actualMessage = exception.getMessage();
            assertTrue(actualMessage.contains(expectedMessage));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Test
    void isSaveAccountIdIsNull() {
        try {
            CardRepositoryImp cardRepository = new CardRepositoryImp(dataSource);
            Exception exception = assertThrows(NotSavedSubEntityException.class, () -> {
                cardRepository.save(EXPECTED_SAVE_ACCOUNT_ID_IS_NULL);
            });
            String expectedMessage = "Значение accountId =  null";
            String actualMessage = exception.getMessage();
            assertTrue(actualMessage.contains(expectedMessage));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Test
    void isUpdate() {
        try {
            CardRepositoryImp cardRepository = new CardRepositoryImp(dataSource);
            cardRepository.update(EXPECTED_UPDATE);
            Card check = cardRepository.get(3l).get();
            assertEquals(check, EXPECTED_UPDATE);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Test
    void isUpdateCardNumberIdIsNull() {
        try {
            CardRepositoryImp cardRepository = new CardRepositoryImp(dataSource);
            Exception exception = assertThrows(NotSavedSubEntityException.class, () -> {
                cardRepository.update(EXPECTED_UPDATE_CARD_NUMBER_IS_NULL);
            });
            String expectedMessage = "Значение cardNumber =  null";
            String actualMessage = exception.getMessage();
            assertTrue(actualMessage.contains(expectedMessage));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Test
    void isUpdateOwnerIdIsNull() {
        try {
            CardRepositoryImp cardRepository = new CardRepositoryImp(dataSource);
            Exception exception = assertThrows(NotSavedSubEntityException.class, () -> {
                cardRepository.update(EXPECTED_UPDATE_USER_ID_IS_NULL);
            });
            String expectedMessage = "Значение userId =  null";
            String actualMessage = exception.getMessage();
            assertTrue(actualMessage.contains(expectedMessage));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Test
    void isUpdateAccountIdIsNull() {
        Exception exception = null;
        try {
            CardRepositoryImp cardRepository = new CardRepositoryImp(dataSource);
            exception = assertThrows(NotSavedSubEntityException.class, () -> {
                cardRepository.update(EXPECTED_UPDATE_ACCOUNT_ID_IS_NULL);
            });
            String expectedMessage = "Значение accountId =  null";
            String actualMessage = exception.getMessage();
            assertTrue(actualMessage.contains(expectedMessage));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Test
    void isDelete() {

        List<Card> check = null;
        try {
            CardRepositoryImp cardRepository = new CardRepositoryImp(dataSource);
            cardRepository.delete(2l);
            check = cardRepository.getAll();
            assertEquals(check, EXPECTED_DELETE);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Test
    void isGetByNumber() {

        Card check = null;
        try {
            CardRepositoryImp cardRepository = new CardRepositoryImp(dataSource);
            check = cardRepository.getByNumber("7356 9264 7634 2534").get();
            assertEquals(check, EXPECTED_GET_BY_ID);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Test
    void isGetByNumberIsEmpty() {
        Optional check = null;
        try {
            CardRepositoryImp cardRepository = new CardRepositoryImp(dataSource);
            check = cardRepository.getByNumber("7356 9264 7634 0000");
            assertEquals(check, Optional.empty());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Test
    void isConnection() {
        JdbcDataSource ds = new JdbcDataSource();
        assertThrows(SQLException.class, () -> {
            new CardRepositoryImp(ds);
        });
    }
}
