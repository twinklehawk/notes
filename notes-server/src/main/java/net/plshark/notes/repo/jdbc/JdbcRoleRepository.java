package net.plshark.notes.repo.jdbc;

import java.sql.PreparedStatement;
import java.util.Objects;

import javax.inject.Named;
import javax.inject.Singleton;

import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import net.plshark.notes.Role;
import net.plshark.notes.repo.RoleRepository;

/**
 * Role repository that uses JDBC
 */
@Named
@Singleton
public class JdbcRoleRepository implements RoleRepository {

    private static final String INSERT = "INSERT INTO roles (name) VALUES (?)";
    private static final String DELETE = "DELETE FROM roles WHERE id = ?";

    private final JdbcOperations jdbc;
    //private final RoleRowMapper roleRowMapper = new RoleRowMapper();

    /**
     * Create a new instance
     * @param jdbc the JDBC object to use to interact with the database
     */
    public JdbcRoleRepository(JdbcOperations jdbc) {
        this.jdbc = Objects.requireNonNull(jdbc, "jdbc cannot be null");
    }

    @Override
    public Role insert(Role role) {
        if (role.getId().isPresent())
            throw new IllegalArgumentException("Cannot insert role with ID already set");

        GeneratedKeyHolder holder = new GeneratedKeyHolder();
        jdbc.update(con -> {
                PreparedStatement stmt = con.prepareStatement(INSERT, new int[] { 1 });
                stmt.setString(1, role.getName());
                return stmt;
            }, holder);
        role.setId(holder.getKey().longValue());
        return role;
    }

    @Override
    public void delete(long roleId) {
        jdbc.update(DELETE, stmt -> stmt.setLong(1, roleId));
    }
}
