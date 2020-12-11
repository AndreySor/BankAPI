package com.sber.services;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import org.h2.jdbcx.JdbcDataSource;

public class ServiceDataSource {
    private static DataSource dataSource;

    static {
        HikariConfig config = new HikariConfig();
        config.setUsername("sa");
        config.setPassword("");
//        config.setJdbcUrl("jdbc:h2:~/test;INIT=runscript from 'src/main/resources/schema.sql'\\;runscript from 'src/test/resources/data.sql'");
        config.setJdbcUrl("jdbc:h2:~/test");

        dataSource = new HikariDataSource(config);
//        JdbcDataSource ds = new JdbcDataSource();
//        ds.setUser("sa");
//        ds.setPassword("");
//        ds.setUrl("jdbc:h2:~/test;INIT=runscript from 'src/main/resources/schema.sql'\\;runscript from 'src/main/resources/data.sql'");
//        dataSource = ds;
    }

    private ServiceDataSource() {};

    public static DataSource getDataSource() {
        return dataSource;
    }
}
