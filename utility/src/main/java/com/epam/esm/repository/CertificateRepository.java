package com.epam.esm.repository;

import com.epam.esm.exception.RepositoryException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class CertificateRepository implements Repository {

    private static final String COUNT_CERTIFICATES = "select count(*) from certificate";

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
