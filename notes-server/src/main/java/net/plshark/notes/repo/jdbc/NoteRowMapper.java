package net.plshark.notes.repo.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import net.plshark.notes.Note;

/**
 * Maps result rows to Note objects
 */
class NoteRowMapper implements RowMapper<Note> {

    @Override
    public Note mapRow(ResultSet rs, int rowNum) throws SQLException {
        Note note = new Note();

        note.setId(rs.getLong("id"));
        note.setOwnerId(rs.getLong("owner_id"));
        note.setCorrelationId(rs.getLong("correlation_id"));
        note.setTitle(rs.getString("title"));
        note.setContent(rs.getString("content"));

        return note;
    }
}
