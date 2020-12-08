package com.sber.repositories;

import com.sber.models.Account;
import com.sber.models.User;
import org.h2.jdbcx.JdbcDataSource;
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

public class TestAccountRepository {
    DataSource dataSource;

    final  Account EXPECTED_GET_BY_ID = Account.builder()
                    .accountId(3l)
                    .accountNumber("7356 9264 7634 2534")
                    .balance(new BigDecimal("2000.00"))
                    .owner(User.builder()
                            .userId(3l)
                            .firstName("Anton")
                            .lastName("Zubov")
                            .build())
                    .build();

    final List<Account> EXPECTED_FIND_ALL =
            new ArrayList<>(asList(Account.builder()
                                    .accountId(1l)
                                    .accountNumber("4356 3245 1234 7345")
                                    .balance(new BigDecimal("245.30"))
                                    .owner(User.builder()
                                            .userId(5l)
                                            .firstName("Marsel")
                                            .lastName("Abramov")
                                            .build())
                                    .build(),
                    Account.builder()
                                    .accountId(2l)
                                    .accountNumber("4356 3834 1234 2855")
                                    .balance(new BigDecimal("30045.23"))
                                    .owner(User.builder()
                                            .userId(1l)
                                            .firstName("Andrey")
                                            .lastName("Sidorov")
                                            .build())
                                    .build(),
                    Account.builder()
                                    .accountId(3l)
                                    .accountNumber("7356 9264 7634 2534")
                                    .balance(new BigDecimal("2000.00"))
                                    .owner(User.builder()
                                            .userId(3l)
                                            .firstName("Anton")
                                            .lastName("Zubov")
                                            .build())
                                    .build(),
                    Account.builder()
                                    .accountId(4l)
                                    .accountNumber("2054 6334 2299 8376")
                                    .balance(new BigDecimal("7043.54"))
                                    .owner(User.builder()
                                            .userId(2l)
                                            .firstName("Sergey")
                                            .lastName("Larin")
                                            .build())
                                    .build(),
                    Account.builder()
                                    .accountId(5l)
                                    .accountNumber("9934 9264 3456 4423")
                                    .balance(new BigDecimal("12900.40"))
                                    .owner(User.builder()
                                            .userId(4l)
                                            .firstName("Zaur")
                                            .lastName("Ivanov")
                                            .build())
                                    .build()));

    final Account EXPECTED_SAVE =
            Account.builder()
                            .accountId(6l)
                            .accountNumber("9934 9264 3456 4423")
                            .balance(new BigDecimal("12900.40"))
                            .owner(User.builder()
                                    .userId(4l)
                                    .firstName("Zaur")
                                    .lastName("Ivanov")
                                    .build())
                            .build();

    final Account EXPECTED_SAVE_NOT_OWNER_ID =
            Account.builder()
                            .accountId(5l)
                            .accountNumber("9934 9264 3456 4423")
                            .balance(new BigDecimal("12900.40"))
                            .owner(User.builder()
                                    .userId(7l)
                                    .firstName("Zaur")
                                    .lastName("Ivanov")
                                    .build())
                            .build();

    final Account EXPECTED_SAVE_OWNER_ID_IS_NULL =
            Account.builder()
                            .accountId(5l)
                            .accountNumber("9934 9264 3456 4423")
                            .balance(new BigDecimal("12900.40"))
                            .owner(User.builder()
                                    .userId(null)
                                    .firstName("Zaur")
                                    .lastName("Ivanov")
                                    .build())
                            .build();

    final Account EXPECTED_UPDATE = Account.builder()
                    .accountId(4l)
                    .accountNumber("2353 6334 9865 8376")
                    .balance(new BigDecimal("1273.23"))
                    .owner(User.builder()
                            .userId(3l)
                            .firstName("Anton")
                            .lastName("Zubov")
                            .build())
                    .build();

    final Account EXPECTED_UPDATE_ACCOUNT_NUMBER_IS_NULL = Account.builder()
                    .accountId(4l)
                    .accountNumber(null)
                    .balance(new BigDecimal("7043.54"))
                    .owner(User.builder()
                            .userId(2l)
                            .firstName("Sergey")
                            .lastName("Larin")
                            .build())
                    .build();

    final Account EXPECTED_UPDATE_USER_ID_IS_NULL = Account.builder()
                    .accountId(4l)
                    .accountNumber("2054 6334 2299 8376")
                    .balance(new BigDecimal("7043.54"))
                    .owner(User.builder()
                            .userId(null)
                            .firstName("Sergey")
                            .lastName("Larin")
                            .build())
                    .build();

    final List<Account> EXPECTED_DELETE =
            new ArrayList<>(asList(Account.builder()
                                    .accountId(1l)
                                    .accountNumber("4356 3245 1234 7345")
                                    .balance(new BigDecimal("245.30"))
                                    .owner(User.builder()
                                            .userId(5l)
                                            .firstName("Marsel")
                                            .lastName("Abramov")
                                            .build())
                                    .build(),
                    Account.builder()
                                    .accountId(3l)
                                    .accountNumber("7356 9264 7634 2534")
                                    .balance(new BigDecimal("2000.00"))
                                    .owner(User.builder()
                                            .userId(3l)
                                            .firstName("Anton")
                                            .lastName("Zubov")
                                            .build())
                                    .build(),
                    Account.builder()
                                    .accountId(4l)
                                    .accountNumber("2054 6334 2299 8376")
                                    .balance(new BigDecimal("7043.54"))
                                    .owner(User.builder()
                                            .userId(2l)
                                            .firstName("Sergey")
                                            .lastName("Larin")
                                            .build())
                                    .build(),
                    Account.builder()
                                    .accountId(5l)
                                    .accountNumber("9934 9264 3456 4423")
                                    .balance(new BigDecimal("12900.40"))
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
            AccountRepositoryImp accountRepository = new AccountRepositoryImp(dataSource);
            Account check = accountRepository.get(3l).get();
            assertEquals(check, EXPECTED_GET_BY_ID);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Test
    void isGetIsEmpty() {
        try {
            AccountRepositoryImp accountRepository = new AccountRepositoryImp(dataSource);
            Optional check = accountRepository.get(9l);
            assertEquals(check, Optional.empty());
        } catch (SQLException throwables) {
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
            throwables.printStackTrace();
        }
    }

    @Test
    void isSave() {
        try {
            AccountRepositoryImp accountRepository = new AccountRepositoryImp(dataSource);
            accountRepository.save(EXPECTED_SAVE);
            Account check = accountRepository.get(6l).get();
            assertEquals(check, EXPECTED_SAVE);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Test
    void isSaveNotOwnerId() {
        try {
            AccountRepositoryImp accountRepository = new AccountRepositoryImp(dataSource);
            Exception exception = assertThrows(NotSavedSubEntityException.class, () -> {
                accountRepository.save(EXPECTED_SAVE_NOT_OWNER_ID);
            });
            String expectedMessage = "В таблице sber_users не существует записи с id =  7";
            String actualMessage = exception.getMessage();
            assertTrue(actualMessage.contains(expectedMessage));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Test
    void isSaveNotOwnerIdIsNull() {
        try {
            AccountRepositoryImp accountRepository = new AccountRepositoryImp(dataSource);
            Exception exception = assertThrows(NotSavedSubEntityException.class, () -> {
                accountRepository.save(EXPECTED_SAVE_OWNER_ID_IS_NULL);
            });
            String expectedMessage = "Значение userId =  null";
            String actualMessage = exception.getMessage();
            assertTrue(actualMessage.contains(expectedMessage));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Test
    void isUpdate() {
        try {
            AccountRepositoryImp accountRepository = new AccountRepositoryImp(dataSource);
            accountRepository.update(EXPECTED_UPDATE);
            Account check = accountRepository.get(4l).get();
            assertEquals(check, EXPECTED_UPDATE);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Test
    void isUpdateOwnerIdIsNull() {
        try {
            AccountRepositoryImp accountRepository = new AccountRepositoryImp(dataSource);
            Exception exception = assertThrows(NotSavedSubEntityException.class, () -> {
                accountRepository.update(EXPECTED_UPDATE_USER_ID_IS_NULL);
            });
            String expectedMessage = "Значение userId =  null";
            String actualMessage = exception.getMessage();
            assertTrue(actualMessage.contains(expectedMessage));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Test
    void isUpdateAccountNumberIsNull() {
        try {
            AccountRepositoryImp accountRepository = new AccountRepositoryImp(dataSource);
            Exception exception = assertThrows(NotSavedSubEntityException.class, () -> {
                accountRepository.update(EXPECTED_UPDATE_ACCOUNT_NUMBER_IS_NULL);
            });
            String expectedMessage = "Значение accountNumber =  null";
            String actualMessage = exception.getMessage();
            assertTrue(actualMessage.contains(expectedMessage));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Test
    void isDelete(){
        try {
            AccountRepositoryImp accountRepository = new AccountRepositoryImp(dataSource);
            accountRepository.delete(2l);
            List<Account> check = accountRepository.getAll();
            assertEquals(check, EXPECTED_DELETE);
        } catch (SQLException throwables) {
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
            throwables.printStackTrace();
        }
    }

    @Test
    void isConnection() {
        JdbcDataSource ds = new JdbcDataSource();
        assertThrows(SQLException.class, () -> {
            new AccountRepositoryImp(ds);
        });
    }
}
