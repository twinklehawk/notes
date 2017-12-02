package net.plshark.notes.webservice;

import javax.sql.DataSource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import net.plshark.notes.repo.jdbc.JdbcNotesRepository;
import net.plshark.notes.service.NotesServiceImpl;

/**
 * Application entry point
 */
@SpringBootApplication
@ComponentScan(basePackageClasses = {
        Application.class,
        NotesServiceImpl.class,
        JdbcNotesRepository.class
})
public class Application {

    /**
     * Application entry point
     * @param args command line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    public DataSource dataSource() {
        // TODO
        return null;
    }
}
