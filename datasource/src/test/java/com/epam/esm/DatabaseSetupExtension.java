package com.epam.esm;

import com.epam.esm.config.DataSourceConfig;
import com.epam.esm.config.DataSourceConfiguration;
//import org.flywaydb.core.Flyway;
import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * giftcertificates
 *
 * @author Dzmitry Platonov on 2019-09-24.
 * @version 0.0.1
 */
//@SpringJUnitConfig(DataSourceConfig.class)
//public class DatabaseSetupExtension implements BeforeEachCallback, AfterEachCallback {
//
//    @Autowired
//    private DataSourceConfiguration dataSourceConfiguration;
//
//    private Flyway flyway;
//
//    @Override
//    public void beforeEach(ExtensionContext context) throws Exception {
//        flyway = Flyway.configure()
//                .dataSource(
//                        "jdbc:h2:mem:db;DB_CLOSE_DELAY=-1",
//                        "sa",
//                        "sa")
//                .load();
//        flyway.migrate();
//    }
//
//    @Override
//    public void afterEach(ExtensionContext context) throws Exception {
//        flyway.clean();
//    }
//}
