package com.sber.services;

import org.h2.tools.DeleteDbFiles;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static com.sber.services.ServiceDataSource.getDataSource;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ServiceDataSourceTests {
    @Test
    public void shouldGetConnectionFromDataSource() throws SQLException {
        DeleteDbFiles.execute("~", "test", true);
        DataSource dataSource = getDataSource();
        try (Connection connection = dataSource.getConnection()){
            assertTrue(connection.isValid(1));
        }
    }
}
