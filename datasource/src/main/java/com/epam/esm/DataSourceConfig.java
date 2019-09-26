package com.epam.esm;

import com.epam.esm.db.ConnectionPool;
import com.epam.esm.db.DataSourceConfiguration;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * gift certificates
 *
 * @author Dzmitry Platonov on 2019-09-23.
 * @version 0.0.1
 */
@Configuration
@ComponentScan("com.epam.esm")
@EnableTransactionManagement
@PropertySource(value = { "classpath:application.properties" })
public class DataSourceConfig {

    @Bean
    public static PropertySourcesPlaceholderConfigurer placeHolderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public JdbcTemplate jdbcTemplate(ConnectionPool connectionPool ) {
        return new JdbcTemplate(connectionPool);
    }

    @Bean
    public PlatformTransactionManager transactionManager(ConnectionPool connectionPool) {
        return new DataSourceTransactionManager(connectionPool);
    }

    @Bean(initMethod = "init")
    @Lazy
    public ConnectionPool connectionPool(DataSourceConfiguration dataSourceConfiguration) {

        return new ConnectionPool(dataSourceConfiguration, dataSource(dataSourceConfiguration));
    }

    @Bean
    public DataSource dataSource(DataSourceConfiguration dataSourceConfiguration) {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(dataSourceConfiguration.getDbDriver());
        dataSource.setUrl(dataSourceConfiguration.getJdbcUrl());
        dataSource.setUsername(dataSourceConfiguration.getUser());
        dataSource.setPassword(dataSourceConfiguration.getPassword());
        return dataSource;
    }

    @Bean
    public DataSourceConfiguration dataSourceConfiguration(Environment env) {
        DataSourceConfiguration dataSourceConfiguration = new DataSourceConfiguration();
        dataSourceConfiguration.setDbDriver(env.getProperty("jdbc.driverClassName"));
        dataSourceConfiguration.setJdbcUrl(env.getProperty("jdbc.url"));
        dataSourceConfiguration.setUser(env.getProperty("jdbc.username"));
        dataSourceConfiguration.setPassword(env.getProperty("jdbc.password"));
        dataSourceConfiguration.setPoolSize(Integer.parseInt(env.getProperty("jdbc.poolsize")));
        return dataSourceConfiguration;
    }
}
