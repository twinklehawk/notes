package net.plshark.notes.repo.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import net.plshark.notes.Note;

/**
 * Tests for {@link NoteRowMapper}
 */
public class NoteRowMapperTest {

    /**
     * Verify a row is mapped to a note correctly
     * @throws SQLException unexpected
     */
    @Test
    public void mapRowTest() throws SQLException {
        NoteRowMapper mapper = new NoteRowMapper();
        ResultSet rs = Mockito.mock(ResultSet.class);
        Mockito.when(rs.getLong("id")).thenReturn(5L);
        Mockito.when(rs.getLong("owner_id")).thenReturn(6L);
        Mockito.when(rs.getLong("correlation_id")).thenReturn(7L);
        Mockito.when(rs.getString("title")).thenReturn("title");
        Mockito.when(rs.getString("content")).thenReturn("note");

        Note note = mapper.mapRow(rs, 2);
        Assert.assertEquals(5L, note.getId().getAsLong());
        Assert.assertEquals(6L, note.getOwnerId());
        Assert.assertEquals(7L, note.getCorrelationId());
        Assert.assertEquals("title", note.getTitle());
        Assert.assertEquals("note", note.getContent());
    }
}
