package com.epam.esm.config;

/**
 * gift certificates
 *
 * @author Dzmitry Platonov on 2019-09-23.
 * @version 0.0.1
 */
public class DataSourceConfiguration {

    private String user;
    private String password;
    private int poolSize;
    private String dbDriver;
    private String jdbcUrl;

    public DataSourceConfiguration() {
    }

    public DataSourceConfiguration(String user, String password, int poolSize, String dbDriver, String jdbcUrl) {
        this.user = user;
        this.password = password;
        this.poolSize = poolSize;
        this.dbDriver = dbDriver;
        this.jdbcUrl = jdbcUrl;
    }


    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPoolSize() {
        return poolSize;
    }

    public void setPoolSize(int poolSize) {
        this.poolSize = poolSize;
    }

    public String getDbDriver() {
        return dbDriver;
    }

    public void setDbDriver(String dbDriver) {
        this.dbDriver = dbDriver;
    }

    public String getJdbcUrl() {
        return jdbcUrl;
    }

    public void setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }
}
