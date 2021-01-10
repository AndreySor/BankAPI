package com.sber.services;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

public class ServiceDataSource {
    private static DataSource dataSource;

    static {
        HikariConfig config = new HikariConfig();
        config.setUsername("sa");
        config.setPassword("");
        config.setJdbcUrl("jdbc:h2:~/test;AUTO_SERVER=TRUE;Mode=Oracle");
        config.setMaximumPoolSize(10);

        dataSource = new HikariDataSource(config);
    }

    private ServiceDataSource() {};

    public static synchronized DataSource getDataSource() {
        return dataSource;
    }
}
