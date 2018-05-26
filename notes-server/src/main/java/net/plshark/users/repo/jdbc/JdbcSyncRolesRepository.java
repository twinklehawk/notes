package net.plshark.users.repo.jdbc;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.inject.Named;
import javax.inject.Singleton;

import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.JdbcUpdateAffectedIncorrectNumberOfRowsException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import net.plshark.jdbc.SafePreparedStatementCreator;
import net.plshark.users.Role;
import net.plshark.users.repo.SyncRolesRepository;

/**
 * Role repository that uses JDBC
 */
@Named
@Singleton
public class JdbcSyncRolesRepository implements SyncRolesRepository {

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
    public JdbcSyncRolesRepository(JdbcOperations jdbc) {
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
        Long id = Optional.ofNullable(holder.getKey())
                .map(num -> num.longValue())
                .orElseThrow(() -> new JdbcUpdateAffectedIncorrectNumberOfRowsException(INSERT, 1, 0));
        return new Role(id, role.getName());
    }

    @Override
    public void delete(long roleId) {
        jdbc.update(DELETE, stmt -> stmt.setLong(1, roleId));
    }

    @Override
    public Optional<Role> getForId(long id) {
        List<Role> results = jdbc.query(SELECT_BY_ID, stmt -> stmt.setLong(1, id), roleRowMapper);
        return Optional.ofNullable(DataAccessUtils.singleResult(results));
    }

    @Override
    public Optional<Role> getForName(String name) {
        Objects.requireNonNull(name, "name cannot be null");
        List<Role> results = jdbc.query(SELECT_BY_NAME, stmt -> stmt.setString(1, name), roleRowMapper);
        return Optional.ofNullable(DataAccessUtils.singleResult(results));
    }
}
