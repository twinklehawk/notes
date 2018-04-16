package net.plshark.users.repo.jdbc;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.inject.Named;
import javax.inject.Singleton;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import net.plshark.jdbc.SafePreparedStatementCreator;
import net.plshark.users.User;
import net.plshark.users.repo.UsersRepository;

/**
 * User repository that uses JDBC
 */
@Named
@Singleton
public class JdbcUsersRepository implements UsersRepository {

    private static final String SELECT_BY_USERNAME = "SELECT * FROM users WHERE username = ?";
    private static final String SELECT_BY_ID = "SELECT * FROM users WHERE id = ?";
    private static final String INSERT = "INSERT INTO users (username, password) VALUES (?, ?)";
    private static final String DELETE = "DELETE FROM users WHERE id = ?";
    private static final String UPDATE_PASSWORD = "UPDATE users SET password = ? WHERE id = ? AND password = ?";

    private final JdbcOperations jdbc;
    private final UserRowMapper userRowMapper = new UserRowMapper();

    /**
     * Create a new instance
     * @param jdbc the JDBC object to use to interact with the database
     */
    public JdbcUsersRepository(JdbcOperations jdbc) {
        this.jdbc = Objects.requireNonNull(jdbc, "jdbc cannot be null");
    }

    @Override
    public Optional<User> getForUsername(String username) {
        Objects.requireNonNull(username, "username cannot be null");
        List<User> results = jdbc.query(SELECT_BY_USERNAME, stmt -> stmt.setString(1, username), userRowMapper);
        return Optional.ofNullable(DataAccessUtils.singleResult(results));
    }

    @Override
    public User insert(User user) {
        if (user.getId().isPresent())
            throw new IllegalArgumentException("Cannot insert user with ID already set");

        GeneratedKeyHolder holder = new GeneratedKeyHolder();
        jdbc.update(new SafePreparedStatementCreator(
                con -> con.prepareStatement(INSERT, new int[] { 1 }),
                stmt-> {
                    stmt.setString(1, user.getUsername());
                    stmt.setString(2, user.getPassword().get());
                }),
            holder);
        return new User(holder.getKey().longValue(), user.getUsername(), user.getPassword().get());
    }

    @Override
    public void delete(long userId) {
        jdbc.update(DELETE, stmt -> stmt.setLong(1, userId));
    }

    @Override
    public Optional<User> getForId(long id) {
        List<User> results = jdbc.query(SELECT_BY_ID, stmt -> stmt.setLong(1, id), userRowMapper);
        return Optional.ofNullable(DataAccessUtils.singleResult(results));
    }

    @Override
    public void updatePassword(long id, String currentPassword, String newPassword) {
        int updates = jdbc.update(UPDATE_PASSWORD, stmt -> {
            stmt.setString(1, newPassword);
            stmt.setLong(2, id);
            stmt.setString(3, currentPassword);
        });
        if (updates == 0)
            throw new EmptyResultDataAccessException("No matching user for password update", 1);
        else if (updates != 1)
            throw new IllegalStateException("Invalid number of rows affected: " + updates);
    }
}
