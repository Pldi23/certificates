package com.epam.esm.repository;

import com.epam.esm.exception.RepositoryException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class CertificateRepository implements Repository {

    private static final String COUNT_CERTIFICATES = "select count(*) from certificate";
    private static final String INSERT_CERTIFICATE = "insert into certificate(active_status, creation_date, description, expiration_date, modification_date, name, price)\n" +
            "    values (true, null, 'description', null, null, 'violates db constraints name', 1)";

    private static DatabaseConfiguration configuration = DatabaseConfiguration.getInstance();


    @Override
    public long count() throws RepositoryException {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(COUNT_CERTIFICATES);
             ResultSet resultSet = statement.executeQuery()) {
            resultSet.next();
            return resultSet.getLong(1);
        } catch (SQLException e) {
            throw new RepositoryException("could not count rows", e);
        }
    }

    @Override
    public boolean insertDbConstraintObject() throws RepositoryException {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_CERTIFICATE)) {
            return statement.execute();
        } catch (SQLException e) {
            throw new RepositoryException("could not count rows", e);
        }
    }

    private Connection getConnection() throws RepositoryException {
        try {
            Class.forName(configuration.getDbDriver());
            return DriverManager.getConnection(configuration.getJdbcUrl(), configuration.getUser(),
                    configuration.getPassword());
        } catch (ClassNotFoundException e) {
            throw new RepositoryException("could not get driver", e);
        } catch (SQLException e) {
            throw new RepositoryException("could not get connection", e);
        }
    }
}
