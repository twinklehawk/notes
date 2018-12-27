package net.plshark.notes.webservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

import net.plshark.notes.service.NotesServiceConfig;
import net.plshark.users.service.UsersServiceConfig;

/**
 * Application entry point
 */
@SpringBootApplication
@Import({
    NotesServiceConfig.class,
    UsersServiceConfig.class
})
@ComponentScan({
    "net.plshark.notes.webservice",
    "net.plshark.notes.repo.config",
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
