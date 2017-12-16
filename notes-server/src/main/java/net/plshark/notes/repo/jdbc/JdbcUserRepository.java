package net.plshark.notes.repo.jdbc;

import java.util.List;
import java.util.Objects;

import javax.inject.Named;
import javax.inject.Singleton;

import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcOperations;

import net.plshark.notes.User;
import net.plshark.notes.repo.UserRepository;

/**
 * User repository that uses JDBC
 */
@Named
@Singleton
public class JdbcUserRepository implements UserRepository {

    private static final String SELECT_BY_USERNAME = "SELECT * FROM users WHERE username = ?";

    private final JdbcOperations jdbc;
    private final UserRowMapper userRowMapper = new UserRowMapper();

    /**
     * Create a new instance
     * @param jdbc the JDBC object to use to interact with the database
     */
    public JdbcUserRepository(JdbcOperations jdbc) {
        this.jdbc = Objects.requireNonNull(jdbc, "jdbc cannot be null");
    }

    @Override
    public User getForUsername(String username) {
        Objects.requireNonNull(username, "username cannot be null");
        List<User> results = jdbc.query(SELECT_BY_USERNAME, stmt -> stmt.setString(1, username), userRowMapper);
        return DataAccessUtils.requiredSingleResult(results);
    }
}
