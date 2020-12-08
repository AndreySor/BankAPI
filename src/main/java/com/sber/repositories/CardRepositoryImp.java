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

public class CardRepositoryImp implements CardRepository<Card>{
    private Connection connection;

    public CardRepositoryImp(DataSource dataSource) throws SQLException{
        try {
            this.connection = dataSource.getConnection();
        } catch (SQLException e) {
            throw new SQLException(e);
        }
    }

    private final String SQL_SELECT_GET_BY_ID = "SELECT * FROM sber_cards \n" +
            "INNER JOIN sber_accounts ON (sber_cards.account_id = sber_accounts.account_id)\n" +
            "INNER JOIN sber_users ON (sber_accounts.owner_id = sber_users.user_id)\n" +
            "WHERE card_id = ?";

    @Override
    public Optional<Card> get(Long id) throws SQLException {
        PreparedStatement statement = null;

        try {
            statement = connection.prepareStatement(SQL_SELECT_GET_BY_ID);
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                Long cardId = resultSet.getLong("card_id");
                String cardNumber = resultSet.getString("card_number");
                Long accountId = resultSet.getLong("account_id");
                String accountNumber = resultSet.getString("account_number");
                BigDecimal balance = resultSet.getBigDecimal("balance");
                Long userId = resultSet.getLong("user_id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                User user = User.builder()
                        .userId(userId)
                        .firstName(firstName)
                        .lastName(lastName)
                        .build();

                return Optional.of(Card.builder()
                                    .cardId(cardId)
                                    .cardNumber(cardNumber)
                                    .account(Account.builder()
                                            .accountId(accountId)
                                            .accountNumber(accountNumber)
                                            .balance(balance)
                                            .owner(user)
                                            .build())
                                    .owner(user)
                                    .build());
            }
        } catch (SQLException e) {
            throw new SQLException(e);
        }

        return Optional.empty();
    }

    private final String SQL_SELECT_GET_ALL = "SELECT * FROM sber_cards \n" +
            "INNER JOIN sber_accounts ON (sber_cards.account_id = sber_accounts.account_id)\n" +
            "INNER JOIN sber_users ON (sber_accounts.owner_id = sber_users.user_id)\n";

    @Override
    public List<Card> getAll() throws SQLException {
        List<Card> cards = new ArrayList<>();
        PreparedStatement statement = null;

        try {
            statement = connection.prepareStatement(SQL_SELECT_GET_ALL);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Long cardId = resultSet.getLong("card_id");
                String cardNumber = resultSet.getString("card_number");
                Long accountId = resultSet.getLong("account_id");
                String accountNumber = resultSet.getString("account_number");
                BigDecimal balance = resultSet.getBigDecimal("balance");
                Long userId = resultSet.getLong("user_id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                User user = User.builder()
                        .userId(userId)
                        .firstName(firstName)
                        .lastName(lastName)
                        .build();

                cards.add(Card.builder()
                        .cardId(cardId)
                        .cardNumber(cardNumber)
                        .account(Account.builder()
                                .accountId(accountId)
                                .accountNumber(accountNumber)
                                .balance(balance)
                                .owner(user)
                                .build())
                        .owner(user)
                        .build());
            }
        } catch (SQLException e) {
            throw new SQLException(e);
        }
        return cards;
    }

    private final String SQL_INSERT_NEW_STRING = "INSERT INTO sber_cards(card_number, account_id, owner_id)\n" +
            "VALUES (?, ?, ?);";

    @Override
    public void save(Card entity) throws SQLException {
        PreparedStatement statement = null;

        if (entity.getOwner().getUserId() == null) {
            throw new NotSavedSubEntityException("Значение userId = ", null);
        } else if (entity.getAccount().getAccountId() == null) {
            throw new NotSavedSubEntityException("Значение accountId = ", null);
        }
        try {
            statement = connection.prepareStatement("SELECT * FROM sber_users WHERE user_id = ?");
            statement.setLong(1, entity.getOwner().getUserId());
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                throw new NotSavedSubEntityException("В таблице sber_users не существует записи с id = ", entity.getOwner().getUserId());
            }
            statement = connection.prepareStatement("SELECT * FROM sber_accounts WHERE account_id = ?");
            statement.setLong(1, entity.getAccount().getAccountId());
            resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                throw new NotSavedSubEntityException("В таблице sber_accounts не существует записи с id = ", entity.getAccount().getAccountId());
            }
            statement = connection.prepareStatement(SQL_INSERT_NEW_STRING, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, entity.getCardNumber());
            statement.setLong(2, entity.getAccount().getAccountId());
            statement.setLong(3, entity.getOwner().getUserId());
            int rows = statement.executeUpdate();
            ResultSet resultset = statement.getGeneratedKeys();
            resultset.next();
            entity.setCardId(resultset.getLong(1));
        } catch (SQLException e) {
            throw new SQLException(e);
        }
    }

    private final String SQL_UPDATE_CARDS = "UPDATE sber_cards\n" +
            "SET card_number = ?, account_id = ?, owner_id = ? \n" +
            "WHERE card_id = ?";

    @Override
    public void update(Card entity) throws SQLException {
        PreparedStatement statement = null;

        try {
            statement = connection.prepareStatement(SQL_UPDATE_CARDS);
            if (entity.getCardNumber() != null) {
                statement.setString(1, entity.getCardNumber());
            } else {
                throw new NotSavedSubEntityException("Значение cardNumber = ", null);
            }
            if (entity.getAccount().getAccountId() != null) {
                statement.setLong(2, entity.getAccount().getAccountId());
            } else {
                throw new NotSavedSubEntityException("Значение accountId = ", null);
            }
            if (entity.getOwner().getUserId() != null) {
                statement.setLong(3, entity.getOwner().getUserId());
            } else {
                throw new NotSavedSubEntityException("Значение userId = ", null);
            }
            statement.setLong(4, entity.getCardId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException(e);
        }
    }

    private final String SQL_DELETE = "DELETE FROM sber_cards WHERE card_id = ?";

    @Override
    public void delete(Long id) throws SQLException {
        PreparedStatement statement;

        try {
            statement = connection.prepareStatement(SQL_DELETE);
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException(e);
        }
    }

    private final String SQL_SELECT_GET_BY_CARD_NUMBER = "SELECT * FROM sber_cards \n" +
            "INNER JOIN sber_accounts ON (sber_cards.account_id = sber_accounts.account_id)\n" +
            "INNER JOIN sber_users ON (sber_accounts.owner_id = sber_users.user_id)\n" +
            "WHERE card_number = ?";

    @Override
    public Optional<Card> getByNumber(String cardNumber) throws SQLException {
        PreparedStatement statement = null;

        try {
            statement = connection.prepareStatement(SQL_SELECT_GET_BY_CARD_NUMBER);
            statement.setString(1, cardNumber);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                Long cardId = resultSet.getLong("card_id");
                Long accountId = resultSet.getLong("account_id");
                String accountNumber = resultSet.getString("account_number");
                BigDecimal balance = resultSet.getBigDecimal("balance");
                Long userId = resultSet.getLong("user_id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");

                User user = User.builder()
                        .userId(userId)
                        .firstName(firstName)
                        .lastName(lastName)
                        .build();

                return Optional.of(Card.builder()
                        .cardId(cardId)
                        .cardNumber(cardNumber)
                        .account(Account.builder()
                                .accountId(accountId)
                                .accountNumber(accountNumber)
                                .balance(balance)
                                .owner(user)
                                .build())
                        .owner(user)
                        .build());
            }
        } catch (SQLException e) {
            throw new SQLException(e);
        }

        return Optional.empty();
    }
}
