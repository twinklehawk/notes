package net.plshark.users.repo.jdbc;

import java.util.List;
import java.util.Objects;

import javax.inject.Named;
import javax.inject.Singleton;

import org.springframework.jdbc.core.JdbcOperations;

import net.plshark.users.Role;
import net.plshark.users.repo.SyncUserRolesRepository;

/**
 * User roles repository that uses JDBC
 */
@Named
@Singleton
public class JdbcSyncUserRolesRepository implements SyncUserRolesRepository {

    private static final String SELECT_ROLES_FOR_USER = "SELECT r.id id, r.name name FROM roles r INNER JOIN user_roles ur ON r.id = ur.role_id WHERE ur.user_id = ?";
    private static final String INSERT_USER_ROLE = "INSERT INTO user_roles (user_id, role_id) values (?, ?)";
    private static final String DELETE_USER_ROLE = "DELETE FROM user_roles WHERE user_id = ? AND role_id = ?";
    private static final String DELETE_USER_ROLES_BY_USER = "DELETE FROM user_roles WHERE user_id = ?";
    private static final String DELETE_USER_ROLES_BY_ROLE = "DELETE FROM user_roles WHERE role_id = ?";

    private final JdbcOperations jdbc;
    private final RoleRowMapper roleRowMapper = new RoleRowMapper();

    /**
     * Create a new instance
     * @param jdbc the JDBC object to use to interact with the database
     */
    public JdbcSyncUserRolesRepository(JdbcOperations jdbc) {
        this.jdbc = Objects.requireNonNull(jdbc, "jdbc cannot be null");
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

    @Override
    public void deleteUserRolesForUser(long userId) {
        jdbc.update(DELETE_USER_ROLES_BY_USER, stmt -> {
            stmt.setLong(1, userId);
        });
    }

    @Override
    public void deleteUserRolesForRole(long roleId) {
        jdbc.update(DELETE_USER_ROLES_BY_ROLE, stmt -> {
            stmt.setLong(1, roleId);
        });
    }
}
