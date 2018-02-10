package net.plshark.users.repo.jdbc;

import java.util.List;
import java.util.Objects;

import javax.inject.Named;
import javax.inject.Singleton;

import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import net.plshark.jdbc.SafePreparedStatementCreator;
import net.plshark.users.Role;
import net.plshark.users.repo.RolesRepository;

/**
 * Role repository that uses JDBC
 */
@Named
@Singleton
public class JdbcRolesRepository implements RolesRepository {

    private static final String INSERT = "INSERT INTO roles (name) VALUES (?)";
    private static final String DELETE = "DELETE FROM roles WHERE id = ?";
    private static final String SELECT_BY_ID = "SELECT * FROM roles WHERE id = ?";
    private static final String SELECT_BY_NAME = "SELECT * FROM roles WHERE name = ?";

    private final JdbcOperations jdbc;
    private final RoleRowMapper roleRowMapper = new RoleRowMapper();

    /**
     * Create a new instance
     * @param jdbc the JDBC object to use to interact with the database
     */
    public JdbcRolesRepository(JdbcOperations jdbc) {
        this.jdbc = Objects.requireNonNull(jdbc, "jdbc cannot be null");
    }

    @Override
    public Role insert(Role role) {
        if (role.getId().isPresent())
            throw new IllegalArgumentException("Cannot insert role with ID already set");

        GeneratedKeyHolder holder = new GeneratedKeyHolder();
        jdbc.update(new SafePreparedStatementCreator(
                con -> con.prepareStatement(INSERT, new int[] { 1 }),
                stmt -> stmt.setString(1, role.getName())),
            holder);
        role.setId(holder.getKey().longValue());
        return role;
    }

    @Override
    public void delete(long roleId) {
        jdbc.update(DELETE, stmt -> stmt.setLong(1, roleId));
    }

    @Override
    public Role getForId(long id) {
        List<Role> results = jdbc.query(SELECT_BY_ID, stmt -> stmt.setLong(1, id), roleRowMapper);
        return DataAccessUtils.requiredSingleResult(results);
    }

    @Override
    public Role getForName(String name) {
        Objects.requireNonNull(name, "name cannot be null");
        List<Role> results = jdbc.query(SELECT_BY_NAME, stmt -> stmt.setString(1, name), roleRowMapper);
        return DataAccessUtils.requiredSingleResult(results);
    }
}
