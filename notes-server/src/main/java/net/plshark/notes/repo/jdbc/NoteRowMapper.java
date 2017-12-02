package net.plshark.notes.repo.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import net.plshark.notes.Note;

/**
 * Maps result rows to Note objects
 */
class NoteRowMapper implements RowMapper<Note> {

    /**
     * The note columns in the correct order for use with this RowMapper
     */
    public static final String COLUMNS = "id, owner_id, correlation_id, title, content";

    @Override
    public Note mapRow(ResultSet rs, int rowNum) throws SQLException {
        Note note = new Note();

        note.setId(rs.getLong(1));
        note.setOwnerId(rs.getLong(2));
        note.setCorrelationId(rs.getLong(3));
        note.setTitle(rs.getString(4));
        note.setContent(rs.getString(5));

        return note;
    }
}
