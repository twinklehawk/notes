package net.plshark.notes.repo.jdbc;

import java.util.List;
import java.util.Optional;

import javax.inject.Named;
import javax.inject.Singleton;

import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.JdbcUpdateAffectedIncorrectNumberOfRowsException;
import org.springframework.jdbc.core.JdbcOperations;

import net.plshark.notes.UserNotePermission;
import net.plshark.notes.repo.UserNotePermissionsRepository;

/**
 * User note permission repository that uses JDBC
 */
@Named
@Singleton
public class JdbcUserNotePermissionsRepository implements UserNotePermissionsRepository {

    private static final String GET_BY_USER_NOTE = "SELECT * FROM user_note_permissions WHERE user_id = ? AND note_id = ?";
    private static final String INSERT = "INSERT INTO user_note_permissions (user_id, note_id, readable, writable) VALUES (?, ?, ?, ?)";
    private static final String UPDATE = "UPDATE user_note_permissions SET readable = ?, writable = ? WHERE user_id = ? AND note_id = ?";
    private static final String DELETE_BY_USER_NOTE = "DELETE FROM user_note_permissions WHERE user_id = ? AND note_id = ?";
    private static final String DELETE_BY_NOTE = "DELETE FROM user_note_permissions WHERE note_id = ?";

    private final JdbcOperations jdbc;
    private final UserNotePermissionRowMapper permMapper = new UserNotePermissionRowMapper();

    /**
     * Create a new instance
     * @param jdbc the JDBC object to use to interact with the database
     */
    public JdbcUserNotePermissionsRepository(JdbcOperations jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public Optional<UserNotePermission> getByUserAndNote(long userId, long noteId) {
        List<UserNotePermission> list = jdbc.query(GET_BY_USER_NOTE, stmt -> {
            stmt.setLong(1, userId);
            stmt.setLong(2, noteId);
        }, permMapper);
        return Optional.ofNullable(DataAccessUtils.singleResult(list));
    }

    @Override
    public UserNotePermission insert(UserNotePermission permission) {
        jdbc.update(INSERT, stmt -> {
           stmt.setLong(1, permission.getUserId());
           stmt.setLong(2, permission.getNoteId());
           stmt.setBoolean(3, permission.isReadable());
           stmt.setBoolean(4, permission.isWritable());
        });
        return permission;
    }

    @Override
    public void deleteByUserAndNote(long userId, long noteId) {
        jdbc.update(DELETE_BY_USER_NOTE, stmt -> {
            stmt.setLong(1, userId);
            stmt.setLong(2, noteId);
         });
    }

    @Override
    public void deleteByNote(long noteId) {
        jdbc.update(DELETE_BY_NOTE, stmt -> {
            stmt.setLong(1, noteId);
         });
    }

    @Override
    public UserNotePermission update(UserNotePermission permission) {
        int updates = jdbc.update(UPDATE, stmt-> {
            stmt.setBoolean(1, permission.isReadable());
            stmt.setBoolean(2, permission.isWritable());
            stmt.setLong(3, permission.getUserId());
            stmt.setLong(4, permission.getNoteId());
        });
        if (updates != 1)
            throw new JdbcUpdateAffectedIncorrectNumberOfRowsException(UPDATE, 1, updates);
        return permission;
    }
}
