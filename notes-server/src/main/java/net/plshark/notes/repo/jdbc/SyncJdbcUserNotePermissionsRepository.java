package net.plshark.notes.repo.jdbc;

import java.util.List;
import java.util.Optional;

import javax.inject.Named;
import javax.inject.Singleton;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.JdbcUpdateAffectedIncorrectNumberOfRowsException;
import org.springframework.jdbc.core.JdbcOperations;

import net.plshark.notes.UserNotePermission;

/**
 * User note permission repository that uses JDBC
 */
@Named
@Singleton
public class SyncJdbcUserNotePermissionsRepository {

    private static final String GET_BY_USER_NOTE = "SELECT * FROM user_note_permissions WHERE username = ? AND note_id = ?";
    private static final String INSERT = "INSERT INTO user_note_permissions (username, note_id, readable, writable) VALUES (?, ?, ?, ?)";
    private static final String UPDATE = "UPDATE user_note_permissions SET readable = ?, writable = ? WHERE username = ? AND note_id = ?";
    private static final String DELETE_BY_USER_NOTE = "DELETE FROM user_note_permissions WHERE username = ? AND note_id = ?";
    private static final String DELETE_BY_NOTE = "DELETE FROM user_note_permissions WHERE note_id = ?";

    private final JdbcOperations jdbc;
    private final UserNotePermissionRowMapper permMapper = new UserNotePermissionRowMapper();

    /**
     * Create a new instance
     * @param jdbc the JDBC object to use to interact with the database
     */
    public SyncJdbcUserNotePermissionsRepository(JdbcOperations jdbc) {
        this.jdbc = jdbc;
    }

    /**
     * Get a user's permissions for a note
     * @param username the username
     * @param noteId the note ID
     * @return the permissions, may be empty if the user has no permissions for the note
     * @throws DataAccessException if the query fails
     */
    public Optional<UserNotePermission> getByUserAndNote(String username, long noteId) {
        List<UserNotePermission> list = jdbc.query(GET_BY_USER_NOTE, stmt -> {
            stmt.setString(1, username);
            stmt.setLong(2, noteId);
        }, permMapper);
        return Optional.ofNullable(DataAccessUtils.singleResult(list));
    }

    /**
     * Save a new permission
     * @param permission the permission
     * @return the saved permission
     * @throws DataAccessException if the insert fails
     */
    public UserNotePermission insert(UserNotePermission permission) {
        jdbc.update(INSERT, stmt -> {
           stmt.setString(1, permission.getUsername());
           stmt.setLong(2, permission.getNoteId());
           stmt.setBoolean(3, permission.isReadable());
           stmt.setBoolean(4, permission.isWritable());
        });
        return permission;
    }

    /**
     * Delete a user's permission for a note
     * @param username the username
     * @param noteId the note ID
     */
    public void deleteByUserAndNote(String username, long noteId) {
        jdbc.update(DELETE_BY_USER_NOTE, stmt -> {
            stmt.setString(1, username);
            stmt.setLong(2, noteId);
         });
    }

    /**
     * Delete all permissions for a note
     * @param noteId the note ID
     */
    public void deleteByNote(long noteId) {
        jdbc.update(DELETE_BY_NOTE, stmt -> {
            stmt.setLong(1, noteId);
         });
    }

    /**
     * Update an existing permission
     * @param permission the permission to update
     * @return the updated permission
     * @throws DataAccessException if the update fails
     */
    public UserNotePermission update(UserNotePermission permission) {
        int updates = jdbc.update(UPDATE, stmt-> {
            stmt.setBoolean(1, permission.isReadable());
            stmt.setBoolean(2, permission.isWritable());
            stmt.setString(3, permission.getUsername());
            stmt.setLong(4, permission.getNoteId());
        });
        if (updates != 1)
            throw new JdbcUpdateAffectedIncorrectNumberOfRowsException(UPDATE, 1, updates);
        return permission;
    }
}
