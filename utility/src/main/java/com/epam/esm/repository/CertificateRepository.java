package com.epam.esm.repository;

import com.epam.esm.exception.RepositoryException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * utility
 *
 * @author Dzmitry Platonov on 2019-12-13.
 * @version 0.0.1
 */
public class CertificateRepository implements Repository {

    private static DatabaseConfiguration configuration = DatabaseConfiguration.getInstance();


    @Override
    public long count() {
        try (PreparedStatement statement = getConnection().prepareStatement("select count(*) from certificate");
             ResultSet resultSet = statement.executeQuery()) {
            resultSet.next();
            return resultSet.getLong(1);
        } catch (SQLException e) {
            throw new RepositoryException("could not count rows", e);
        }
    }

    private Connection getConnection() {
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
