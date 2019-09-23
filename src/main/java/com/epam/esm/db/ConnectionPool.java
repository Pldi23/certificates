package com.epam.esm.db;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * gift certificates
 *
 * @author Dzmitry Platonov on 2019-09-23.
 * @version 0.0.1
 */
public class ConnectionPool implements DataSource {

    private static final Logger log = LogManager.getLogger();

    private DataSourceConfiguration configuration;
    private BlockingQueue<Connection> connections;

    public ConnectionPool() {
    }

    public ConnectionPool(DataSourceConfiguration dataSourceConfiguration) {
        this.configuration = dataSourceConfiguration;
        init();
    }

    private void init() {
        try {
            Class.forName(configuration.getDbDriver());
        } catch (ClassNotFoundException e) {
            log.fatal("Driver registration error", e);
            throw new RuntimeException("Database driver connection failed", e);
        }
        connections = new ArrayBlockingQueue<>(configuration.getPoolSize());
        for (int i = 0; i < configuration.getPoolSize(); i++) {
            connections.add(createConnection());
        }
        log.debug("Connection pool initialized with " + connections.size() + " connections");
    }

    private Connection createConnection() {
        try {
            log.trace("Connection created");
            return DriverManager.getConnection(configuration.getJdbcUrl(), configuration.getUser(),
                    configuration.getPassword());
        } catch (SQLException e) {
            log.fatal("Can't create database connection", e);
            throw new RuntimeException("Database connection failed", e);
        }
    }

    public Connection getConnection() {
        log.trace("Connection taken");
        try {
            return connections.take();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Interruption during getting connection");
        }
    }

    @Override
    public Connection getConnection(String username, String password) {
        throw new UnsupportedOperationException();
    }

    @Override
    public PrintWriter getLogWriter() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setLogWriter(PrintWriter out) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setLoginTimeout(int seconds) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getLoginTimeout() {
        throw new UnsupportedOperationException();
    }

    @Override
    public java.util.logging.Logger getParentLogger() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T unwrap(Class<T> iface) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) {
        throw new UnsupportedOperationException();
    }
}