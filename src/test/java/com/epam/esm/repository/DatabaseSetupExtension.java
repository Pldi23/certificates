package com.epam.esm.repository;

import com.epam.esm.AppConfig;
import com.epam.esm.db.DataSourceConfiguration;
import org.flywaydb.core.Flyway;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * giftcertificates
 *
 * @author Dzmitry Platonov on 2019-09-24.
 * @version 0.0.1
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class})
public class DatabaseSetupExtension {

    @Autowired
    private DataSourceConfiguration dataSourceConfiguration;

    private Flyway flyway;

    @Before
    public void setUp() {
        flyway = Flyway.configure().dataSource(dataSourceConfiguration.getJdbcUrl(),
                dataSourceConfiguration.getUser(), dataSourceConfiguration.getPassword()).load();
        flyway.migrate();
    }

    @After
    public void tierDown() {
        flyway.clean();
    }
}