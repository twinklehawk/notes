package net.plshark.notes.repo.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Spring configuration for the notes JDBC repository
 */
@Configuration
@ComponentScan("net.plshark.notes.repo.jdbc")
@EnableTransactionManagement
public class NotesRepoJdbcConfig {

    /**
     * Build the transaction manager
     * @param ds the data source
     * @return the transaction manager
     */
    @Bean
    public PlatformTransactionManager txManager(DataSource ds) {
        return new DataSourceTransactionManager(ds);
    }
}
