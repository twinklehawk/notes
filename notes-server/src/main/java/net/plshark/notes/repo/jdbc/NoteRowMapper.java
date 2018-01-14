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
        return new Note(rs.getLong("id"), rs.getLong("correlation_id"),
                rs.getString("title"), rs.getString("content"));
    }
}
