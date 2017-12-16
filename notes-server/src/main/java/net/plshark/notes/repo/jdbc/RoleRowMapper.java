package net.plshark.notes.repo.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import net.plshark.notes.Role;

/**
 * Maps result rows to User objects
 */
class RoleRowMapper implements RowMapper<Role> {

    @Override
    public Role mapRow(ResultSet rs, int rowNum) throws SQLException {
        Role role = new Role();

        role.setId(rs.getLong("id"));
        role.setName(rs.getString("name"));

        return role;
    }
}
