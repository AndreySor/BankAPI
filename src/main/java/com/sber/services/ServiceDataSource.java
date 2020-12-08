package com.sber.services;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

public class ServiceDataSource {
    private static DataSource ds;

    static {
        HikariConfig config = new HikariConfig();
        config.setUsername("sa");
        config.setPassword("");
        config.setJdbcUrl("jdbc:h2:~/test;AUTO_SERVER=TRUE;Mode=Oracle;INIT=runscript from 'src/main/resources/schema.sql'\\;runscript from 'src/main/resources/data.sql'");

        ds = new HikariDataSource(config);
    }

    private ServiceDataSource() {};

    public static DataSource getDataSource() {
        return ds;
    }
}
