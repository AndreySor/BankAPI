package com.sber.repositories;

import com.sber.models.Account;

import java.sql.SQLException;
import java.util.Optional;

public interface AccountRepository extends DaoRepository<Account>{
    Optional<Account> getByNumber(String accountNumber) throws SQLException;
}
