package net.plshark.notes.webservice;

import net.plshark.notes.service.NotesServiceConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

/**
 * Application entry point
 */
@SpringBootApplication
@Import({
    NotesServiceConfig.class
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
