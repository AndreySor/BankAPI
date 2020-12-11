package com.sber.repositories;

import com.sber.models.Account;
import com.sber.models.Card;
import com.sber.models.User;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepositoryImp implements DaoRepository<User>{

    private DataSource dataSource;

    private final String SQL_SELECT_GET_BY_ID = "SELECT * FROM sber_users \n" +
            "LEFT JOIN sber_accounts ON (sber_users.user_id = sber_accounts.owner_id)\n" +
            "LEFT JOIN sber_cards ON (sber_accounts.account_id = sber_cards.account_id)\n" +
            "WHERE user_id = ? ORDER BY sber_users.user_id, sber_accounts.account_id, sber_cards.card_id";

    private final String SQL_SELECT_GET_ALL = "SELECT * FROM sber_users \n" +
            "LEFT JOIN sber_accounts ON (sber_users.user_id = sber_accounts.owner_id)\n" +
            "LEFT JOIN sber_cards ON (sber_accounts.account_id = sber_cards.account_id)\n" +
            "ORDER BY sber_users.user_id, sber_accounts.account_id, sber_cards.card_id";

    private final String SQL_INSERT_NEW_STRING = "INSERT INTO sber_users(first_name, last_name)\n" +
            "VALUES (?, ?);";

    private final String SQL_UPDATE_USERS = "UPDATE sber_users\n" +
            "SET first_name = ?, last_name = ? \n" +
            "WHERE user_id = ?";

    private final String SQL_DELETE = "DELETE FROM sber_users WHERE user_id = ?";

    public UserRepositoryImp(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Optional<User> get(Long id) throws SQLException{
        User user = null;
        Account account = null;

        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(SQL_SELECT_GET_BY_ID)) {
                statement.setLong(1, id);
                try (ResultSet resultSet = statement.executeQuery()) {

                    while (resultSet.next()) {
                        if (user == null) {
                            user = User.builder()
                                    .userId(resultSet.getLong("user_id"))
                                    .firstName(resultSet.getString("first_name"))
                                    .lastName(resultSet.getString("last_name"))
                                    .accounts(new ArrayList<>())
                                    .cards(new ArrayList<>())
                                    .build();
                        }
                        Long accountId = resultSet.getLong("account_id");
                        if ((account == null || !(account.getAccountId().equals(accountId))) && !(accountId.equals(0l))) {
                            account = Account.builder()
                                    .accountId(accountId)
                                    .accountNumber(resultSet.getString("account_number"))
                                    .balance(resultSet.getBigDecimal("balance"))
                                    .build();
                            user.getAccounts().add(account);
                        }
                        Long cardId = resultSet.getLong("card_id");
                        if (!(cardId.equals(0l))) {
                            user.getCards().add(Card.builder()
                                    .cardId(resultSet.getLong("card_id"))
                                    .cardNumber(resultSet.getString("card_number"))
                                    .account(account)
                                    .build());
                        }
                    }
                }
            }
        }
        if (user == null) {
            return Optional.empty();
        } else {
        return Optional.of(user);
        }
    }

    @Override
    public List<User> getAll() throws SQLException{
        List<User> users = new ArrayList<>();
        User user = null;
        Account account = null;

        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(SQL_SELECT_GET_ALL)) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        Long userId = resultSet.getLong("user_id");
                        if (user == null || !(user.getUserId().equals(userId))) {
                            user = User.builder()
                                    .userId(userId)
                                    .firstName(resultSet.getString("first_name"))
                                    .lastName(resultSet.getString("last_name"))
                                    .accounts(new ArrayList<>())
                                    .cards(new ArrayList<>())
                                    .build();
                            users.add(user);
                        }
                        Long accountId = resultSet.getLong("account_id");
                        if ((account == null || !(account.getAccountId().equals(accountId))) && !(accountId.equals(0l))) {
                            account = Account.builder()
                                    .accountId(accountId)
                                    .accountNumber(resultSet.getString("account_number"))
                                    .balance(resultSet.getBigDecimal("balance"))
                                    .build();
                            user.getAccounts().add(account);
                        }
                        Long cardId = resultSet.getLong("card_id");
                        if (!(cardId.equals(0l))) {
                            user.getCards().add(Card.builder()
                                    .cardId(resultSet.getLong("card_id"))
                                    .cardNumber(resultSet.getString("card_number"))
                                    .account(account)
                                    .build());
                        }
                    }
                }
            }
        }
        return users;
    }

    @Override
    public void save(User entity) throws SQLException {

        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(SQL_INSERT_NEW_STRING, Statement.RETURN_GENERATED_KEYS)) {
                statement.setString(1, entity.getFirstName());
                statement.setString(2, entity.getLastName());
                statement.executeUpdate();
                try (ResultSet resultset = statement.getGeneratedKeys()) {
                    resultset.next();
                    entity.setUserId(resultset.getLong(1));
                }
            }
        }
    }

    @Override
    public void update(User entity) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(SQL_UPDATE_USERS)) {
                statement.setString(1, entity.getFirstName());
                statement.setString(2, entity.getLastName());
                statement.setLong(3, entity.getUserId());
                statement.executeUpdate();
            }
        }
    }

    @Override
    public void delete(Long id) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(SQL_DELETE)) {
                statement.setLong(1, id);
                statement.executeUpdate();
            }
        }
    }
}
