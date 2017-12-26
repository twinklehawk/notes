package net.plshark.notes.repo.jdbc;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;

import javax.inject.Named;
import javax.inject.Singleton;

import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import net.plshark.notes.Role;
import net.plshark.notes.User;
import net.plshark.notes.repo.UserRepository;

/**
 * User repository that uses JDBC
 */
@Named
@Singleton
public class JdbcUserRepository implements UserRepository {

    private static final String SELECT_BY_USERNAME = "SELECT * FROM users WHERE username = ?";
    private static final String SELECT_BY_ID = "SELECT * FROM users WHERE id = ?";
    private static final String INSERT = "INSERT INTO users (username, password) VALUES (?, ?)";
    private static final String DELETE = "DELETE FROM users WHERE id = ?";
    private static final String UPDATE = "UPDATE users SET password = ? WHERE id = ?";
    private static final String SELECT_ROLES_FOR_USER = "SELECT r.id id, r.name name FROM roles r INNER JOIN user_roles ur ON r.id = ur.role_id WHERE ur.user_id = ?";
    private static final String INSERT_USER_ROLE = "INSERT INTO user_roles (user_id, role_id) values (?, ?)";
    private static final String DELETE_USER_ROLE = "DELETE FROM user_roles WHERE user_id = ? AND role_id = ?";

    private final JdbcOperations jdbc;
    private final UserRowMapper userRowMapper = new UserRowMapper();
    private final RoleRowMapper roleRowMapper = new RoleRowMapper();

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

    @Override
    public User insert(User user) {
        if (user.getId().isPresent())
            throw new IllegalArgumentException("Cannot insert user with ID already set");

        GeneratedKeyHolder holder = new GeneratedKeyHolder();
        jdbc.update(con -> {
                PreparedStatement stmt = con.prepareStatement(INSERT, new int[] { 1 });
                stmt.setString(1, user.getUsername());
                stmt.setString(2, user.getPassword());
                return stmt;
            }, holder);
        user.setId(holder.getKey().longValue());
        return user;
    }

    @Override
    public void delete(long userId) {
        jdbc.update(DELETE, stmt -> stmt.setLong(1, userId));
    }

    @Override
    public User getForId(long id) {
        List<User> results = jdbc.query(SELECT_BY_ID, stmt -> stmt.setLong(1, id), userRowMapper);
        return DataAccessUtils.requiredSingleResult(results);
    }

    @Override
    public User update(User user) {
        if (!user.getId().isPresent())
            throw new IllegalArgumentException("Cannot update user without ID");

        jdbc.update(UPDATE, stmt -> {
            stmt.setString(1, user.getPassword());
            stmt.setLong(2, user.getId().getAsLong());
        });
        return user;
    }

    @Override
    public List<Role> getRolesForUser(long userId) {
        return jdbc.query(SELECT_ROLES_FOR_USER, stmt -> stmt.setLong(1, userId), roleRowMapper);
    }

    @Override
    public void insertUserRole(long userId, long roleId) {
        jdbc.update(INSERT_USER_ROLE, stmt -> {
            stmt.setLong(1, userId);
            stmt.setLong(2, roleId);
        });
    }

    @Override
    public void deleteUserRole(long userId, long roleId) {
        jdbc.update(DELETE_USER_ROLE, stmt -> {
            stmt.setLong(1, userId);
            stmt.setLong(2, roleId);
        });
    }
}
