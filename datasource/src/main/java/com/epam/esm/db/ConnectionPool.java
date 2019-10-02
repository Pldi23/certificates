package com.epam.esm.db;

import com.epam.esm.config.DataSourceConfiguration;
import com.epam.esm.exception.ApplicationDataSourceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.datasource.AbstractDataSource;
import org.springframework.jdbc.datasource.SmartDataSource;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * gift certificates
 *
 * @author Dzmitry Platonov on 2019-09-23.
 * @version 0.0.1
 */
public class ConnectionPool extends AbstractDataSource implements SmartDataSource {

    private static final Logger log = LogManager.getLogger();

    private DataSource innerDataSource;
    private DataSourceConfiguration configuration;
    private BlockingQueue<Connection> connections;

    public ConnectionPool() {
    }

    public ConnectionPool(DataSourceConfiguration dataSourceConfiguration, DataSource dataSource) {
        this.configuration = dataSourceConfiguration;
        this.innerDataSource = dataSource;
    }

    @PostConstruct
    public void init() throws SQLException {
        try {
            Class.forName(configuration.getDbDriver());
            connections = new ArrayBlockingQueue<>(configuration.getPoolSize());
            for (int i = 0; i < configuration.getPoolSize(); i++) {
                connections.add(createConnection());
            }
        } catch (ClassNotFoundException e) {
            log.fatal("Driver registration error", e);
            throw new ApplicationDataSourceException("Database driver connection failed", e);
        }
        log.debug("Connection pool initialized with " + connections.size() + " connections");
    }

    private Connection createConnection() throws SQLException {
        return innerDataSource.getConnection();
    }

    @Override
    public Connection getConnection() {
        log.debug("Connection taken");
        try {
            return connections.take();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new ApplicationDataSourceException("Interruption during getting connection", e);
        }
    }


    @Override
    public boolean shouldClose(Connection connection) {
        releaseConnection(connection);
        return false;
    }

    @PreDestroy
    public void destroyPool() {
        for (Connection connection : connections) {
            try {
                connection.close();
            } catch (SQLException e) {
                log.error("exception during destroying connection pool", e);
            }
            this.connections = null;
        }
    }

    private void releaseConnection(Connection connection) {
        connections.add(connection);
        log.debug("Connection released, available connections: " + connections.size());
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return innerDataSource.getConnection(username, password);
    }
}