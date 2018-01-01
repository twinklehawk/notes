package net.plshark.notes.repo.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import net.plshark.notes.entity.NoteEntity;

/**
 * Maps result rows to Note objects
 */
class NoteRowMapper implements RowMapper<NoteEntity> {

    @Override
    public NoteEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new NoteEntity(rs.getLong("id"), rs.getLong("owner_id"), rs.getLong("correlation_id"),
                rs.getString("title"), rs.getString("content"));
    }
}
