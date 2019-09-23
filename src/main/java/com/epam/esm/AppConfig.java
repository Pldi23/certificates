package com.epam.esm;

import com.epam.esm.db.ConnectionPool;
import com.epam.esm.db.DataSourceConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * giftcertificates
 *
 * @author Dzmitry Platonov on 2019-09-23.
 * @version 0.0.1
 */
@Configuration
@ComponentScan("com.epam.esm")
@EnableTransactionManagement
@PropertySource(value = { "classpath:application.properties" })
public class AppConfig {

    @Autowired
    private Environment env;

    @Bean
    public static PropertySourcesPlaceholderConfigurer placeHolderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource, true);
    }

    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    public DataSource dataSource(DataSourceConfiguration dataSourceConfiguration) {
        return new ConnectionPool(dataSourceConfiguration);
    }

    @Bean
    public DataSourceConfiguration dataSourceConfiguration() {
        DataSourceConfiguration dataSourceConfiguration = new DataSourceConfiguration();
        dataSourceConfiguration.setDbDriver(env.getProperty("jdbc.driverClassName"));
        dataSourceConfiguration.setJdbcUrl(env.getProperty("jdbc.url"));
        dataSourceConfiguration.setUser(env.getProperty("jdbc.username"));
        dataSourceConfiguration.setPassword(env.getProperty("jdbc.password"));
        dataSourceConfiguration.setPoolSize(Integer.parseInt(env.getProperty("jdbc.poolsize")));
        return dataSourceConfiguration;
    }

}
