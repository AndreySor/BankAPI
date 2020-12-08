package com.sber.repositories;

import com.sber.models.Card;

import java.sql.SQLException;
import java.util.Optional;

public interface CardRepository<T> extends DaoRepository<Card>{
    Optional<Card> getByNumber(String cardNumber) throws SQLException;
}
