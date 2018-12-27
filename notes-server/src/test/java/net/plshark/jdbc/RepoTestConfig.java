package net.plshark.jdbc;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import net.plshark.notes.repo.config.NotesRepoJdbcConfig;

/**
 * Spring boot configuration for running JDBC integration tests
 */
@SpringBootApplication
@Import({
    NotesRepoJdbcConfig.class
})
public class RepoTestConfig {

}
