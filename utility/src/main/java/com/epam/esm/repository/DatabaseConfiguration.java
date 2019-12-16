package com.epam.esm.repository;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.log4j.Log4j2;

import java.util.ResourceBundle;

/**
 * utility
 *
 * @author Dzmitry Platonov on 2019-12-13.
 * @version 0.0.1
 */
@Data
@AllArgsConstructor
@Log4j2
public class DatabaseConfiguration {

    private static final String DATABASE_PROPERTIES_PATH = "database";
    private static final String DB_HOST = "db.host";
    private static final String DB_USER = "db.user";
    private static final String DB_PASS = "db.password";
    private static final String DB_POOLSIZE = "db.poolsize";
    private static final String DB_PORT = "db.port";
    private static final String DB_NAME = "db.name";
    private static final String DB_DRIVER = "db.driver";
    private static final String JDBC_POSTGRE = "db.jdbc";

    private static DatabaseConfiguration instance;

    private String host;
    private String user;
    private String password;
    private int poolSize;
    private String port;
    private String dbName;
    private String dbDriver;
    private String jdbc;

    public static DatabaseConfiguration getInstance() {
        if (instance == null) {
            instance = init();
        }
        return instance;
    }

    private static DatabaseConfiguration init() {
        ResourceBundle resourceBundle = ResourceBundle.getBundle(DATABASE_PROPERTIES_PATH);
        String host = resourceBundle.getString(DB_HOST);
        String user = resourceBundle.getString(DB_USER);
        String password = resourceBundle.getString(DB_PASS);
        int poolSize = Integer.parseInt(resourceBundle.getString(DB_POOLSIZE));
        String port = resourceBundle.getString(DB_PORT);
        String dbName = resourceBundle.getString(DB_NAME);
        String dbDriver = resourceBundle.getString(DB_DRIVER);
        String dbJdbc = resourceBundle.getString(JDBC_POSTGRE);

        return new DatabaseConfiguration(host, user, password, poolSize, port, dbName, dbDriver, dbJdbc);
    }

    public String getJdbcUrl() {
        return jdbc + host + ":" + port + "/" + dbName;
    }
}

