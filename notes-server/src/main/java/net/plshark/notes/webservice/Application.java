package net.plshark.notes.webservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import net.plshark.notes.repo.jdbc.NotesRepoJdbcConfig;
import net.plshark.notes.service.NotesServiceConfig;
import net.plshark.users.repo.jdbc.UsersRepoJdbcConfig;
import net.plshark.users.service.UsersServiceConfig;

/**
 * Application entry point
 */
@SpringBootApplication
// TODO component scan to pick up repo implementation config
@Import({
    NotesServiceConfig.class,
    NotesRepoJdbcConfig.class,
    UsersServiceConfig.class,
    UsersRepoJdbcConfig.class
})
public class Application {

    /**
     * Application entry point
     * @param args command line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
