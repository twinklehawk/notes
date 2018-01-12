package net.plshark.notes.repo.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import net.plshark.notes.UserNotePermission;

/**
 * Maps result rows to UserNotePermissions
 */
public class UserNotePermissionRowMapper implements RowMapper<UserNotePermission> {

    @Override
    public UserNotePermission mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new UserNotePermission(rs.getLong("user_id"), rs.getLong("note_id"), rs.getBoolean("readable"),
                rs.getBoolean("writable"));
    }
}
