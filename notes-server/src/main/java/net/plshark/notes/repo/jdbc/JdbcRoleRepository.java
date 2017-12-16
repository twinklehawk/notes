package net.plshark.notes.repo.jdbc;

import java.util.List;
import java.util.Objects;

import javax.inject.Named;
import javax.inject.Singleton;

import org.springframework.jdbc.core.JdbcOperations;

import net.plshark.notes.Role;
import net.plshark.notes.repo.RoleRepository;

/**
 * Role repository that uses JDBC
 */
@Named
@Singleton
public class JdbcRoleRepository implements RoleRepository {

    private static final String SELECT_FOR_USER = "SELECT r.id id, r.name name FROM roles r INNER JOIN user_roles ur ON r.id = ur.role_id WHERE ur.user_id = ?";

    private final JdbcOperations jdbc;
    private final RoleRowMapper roleRowMapper = new RoleRowMapper();

    /**
     * Create a new instance
     * @param jdbc the JDBC object to use to interact with the database
     */
    public JdbcRoleRepository(JdbcOperations jdbc) {
        this.jdbc = Objects.requireNonNull(jdbc, "jdbc cannot be null");
    }

    @Override
    public List<Role> getRolesForUser(long userId) {
        return jdbc.query(SELECT_FOR_USER, stmt -> stmt.setLong(1, userId), roleRowMapper);
    }
}
