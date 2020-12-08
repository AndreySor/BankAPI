package com.sber.repositories;

import com.sber.models.Account;
import com.sber.models.Card;
import com.sber.models.User;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AccountRepositoryImp implements AccountRepository {
    private Connection connection;

    public AccountRepositoryImp(DataSource dataSource) throws SQLException{
        this.connection = dataSource.getConnection();
    }

    private final String SQL_SELECT_GET_BY_ID = "SELECT * FROM sber_accounts \n" +
            "INNER JOIN sber_users ON (sber_accounts.owner_id = sber_users.user_id)\n" +
            "WHERE account_id = ?";

    @Override
    public Optional<Account> get(Long id) throws SQLException{
        PreparedStatement statement;

        statement = connection.prepareStatement(SQL_SELECT_GET_BY_ID);
        statement.setLong(1, id);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            Long accountId = resultSet.getLong("account_id");
            String accountNumber = resultSet.getString("account_number");
            BigDecimal balance = resultSet.getBigDecimal("balance");
            Long userId = resultSet.getLong("user_id");
            String firstName = resultSet.getString("first_name");
            String lastName = resultSet.getString("last_name");
            return Optional.of(Account.builder()
                    .accountId(accountId)
                    .accountNumber(accountNumber)
                    .balance(balance)
                    .owner(User.builder()
                            .userId(userId)
                            .firstName(firstName)
                            .lastName(lastName)
                            .build())
                    .build());
        }
        return Optional.empty();
    }

    private final String SQL_SELECT_GET_ALL = "SELECT * FROM sber_accounts \n" +
            "INNER JOIN sber_users ON (sber_accounts.owner_id = sber_users.user_id)\n";

    @Override
    public List<Account> getAll() throws SQLException {
        List<Account> accounts = new ArrayList<>();
        PreparedStatement statement = null;

        statement = connection.prepareStatement(SQL_SELECT_GET_ALL);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            Long accountId = resultSet.getLong("account_id");
            String accountNumber = resultSet.getString("account_number");
            BigDecimal balance = resultSet.getBigDecimal("balance");
            Long userId = resultSet.getLong("user_id");
            String firstName = resultSet.getString("first_name");
            String lastName = resultSet.getString("last_name");
            accounts.add(Account.builder()
                    .accountId(accountId)
                    .accountNumber(accountNumber)
                    .balance(balance)
                    .owner(User.builder()
                            .userId(userId)
                            .firstName(firstName)
                            .lastName(lastName)
                            .build())
                    .build());
        }
        return accounts;
    }

    private final String SQL_INSERT_NEW_STRING = "INSERT INTO sber_accounts(account_number, balance, owner_id)\n" +
            "VALUES (?, ?, ?);";

    @Override
    public void save(Account entity) throws SQLException {
        PreparedStatement statement;

        if (entity.getOwner().getUserId() == null || entity.getOwner().getUserId() == 0L) {
            throw new NotSavedSubEntityException("Значение userId = ", null);
        } else if (entity.getAccountNumber() == null || entity.getAccountNumber().isEmpty()) {
            throw new NotSavedSubEntityException("Значение accountNumber = ", null);
        }
        statement = connection.prepareStatement("SELECT * FROM sber_users WHERE user_id = ?");
        statement.setLong(1, entity.getOwner().getUserId());
        ResultSet resultSet = statement.executeQuery();
        if (!resultSet.next()) {
            throw new NotSavedSubEntityException("В таблице sber_users не существует записи с id = ", entity.getOwner().getUserId());
        }
        statement = connection.prepareStatement(SQL_INSERT_NEW_STRING, Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, entity.getAccountNumber());
        statement.setBigDecimal(2, entity.getBalance());
        statement.setLong(3, entity.getOwner().getUserId());
        int rows = statement.executeUpdate();
        ResultSet resultset = statement.getGeneratedKeys();
        resultset.next();
        entity.setAccountId(resultset.getLong(1));
    }

    private final String SQL_UPDATE_CARDS = "UPDATE sber_accounts\n" +
            "SET account_number = ?, balance = ?, owner_id = ? \n" +
            "WHERE account_id = ?";

    @Override
    public void update(Account entity) throws SQLException {
        PreparedStatement statement;

        statement = connection.prepareStatement(SQL_UPDATE_CARDS);
        if (entity.getAccountNumber() != null && !entity.getAccountNumber().isEmpty()) {
            statement.setString(1, entity.getAccountNumber());
        } else {
            throw new NotSavedSubEntityException("Значение accountNumber = ", null);
        }
        statement.setBigDecimal(2, entity.getBalance());
        if (entity.getOwner().getUserId() != null && entity.getOwner().getUserId() != 0L) {
            statement.setLong(3, entity.getOwner().getUserId());
        } else {
            throw new NotSavedSubEntityException("Значение userId = ", null);
        }
        statement.setLong(4, entity.getAccountId());
        statement.executeUpdate();
    }

    private final String SQL_DELETE = "DELETE FROM sber_accounts WHERE account_id = ?";

    @Override
    public void delete(Long id) throws SQLException {
        PreparedStatement statement;

            statement = connection.prepareStatement(SQL_DELETE);
            statement.setLong(1, id);
            statement.executeUpdate();
    }

    private final String SQL_SELECT_GET_BY_ACCOUNT_NUMBER = "SELECT * FROM sber_accounts \n" +
            "INNER JOIN sber_users ON (sber_accounts.owner_id = sber_users.user_id)\n" +
            "WHERE account_number = ?";

    @Override
    public Optional<Account> getByNumber(String accountNumber) throws SQLException {
        PreparedStatement statement = null;

        statement = connection.prepareStatement(SQL_SELECT_GET_BY_ACCOUNT_NUMBER);
        statement.setString(1, accountNumber);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            Long accountId = resultSet.getLong("account_id");
            BigDecimal balance = resultSet.getBigDecimal("balance");
            Long userId = resultSet.getLong("user_id");
            String firstName = resultSet.getString("first_name");
            String lastName = resultSet.getString("last_name");
            connection.close();

            return Optional.of(Account.builder()
                    .accountId(accountId)
                    .accountNumber(accountNumber)
                    .balance(balance)
                    .owner(User.builder()
                            .userId(userId)
                            .firstName(firstName)
                            .lastName(lastName)
                            .build())
                    .build());
        }
        return Optional.empty();
    }
}
