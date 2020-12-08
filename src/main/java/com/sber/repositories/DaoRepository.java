package com.sber.repositories;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface DaoRepository<T> {
    Optional<T> get(Long id) throws SQLException;
    List<T> getAll() throws SQLException;
    void save(T entity) throws SQLException;
    void update(T entity) throws SQLException;
    void delete(Long id) throws SQLException;
}
