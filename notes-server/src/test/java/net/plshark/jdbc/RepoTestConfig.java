package net.plshark.jdbc;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import net.plshark.notes.repo.config.NotesRepoJdbcConfig;
import net.plshark.users.repo.config.UsersRepoJdbcConfig;

/**
 * Spring boot configuration for running JDBC integration tests
 */
@SpringBootApplication
@Import({
    NotesRepoJdbcConfig.class,
    UsersRepoJdbcConfig.class
})
class RepoTestConfig {

}
