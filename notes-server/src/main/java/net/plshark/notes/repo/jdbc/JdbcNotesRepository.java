package net.plshark.notes.repo.jdbc;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;

import javax.inject.Named;
import javax.inject.Singleton;

import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.JdbcUpdateAffectedIncorrectNumberOfRowsException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import net.plshark.notes.Note;
import net.plshark.notes.repo.NotesRepository;

/**
 * Repository that saves notes in and retrieves notes from a DB using JDBC
 */
@Named
@Singleton
public class JdbcNotesRepository implements NotesRepository {

    private static final String SELECT = "SELECT id, owner_id, correlation_id, title, content FROM notes WHERE id = ?";
    private static final String DELETE = "DELETE FROM notes WHERE id = ?";
    private static final String DELETE_ALL = "DELETE FROM notes";
    private static final String UPDATE = "UPDATE notes SET owner_id = ?, correlation_id = ?, title = ?, content = ? WHERE id = ?";
    private static final String INSERT = "INSERT INTO notes (owner_id, correlation_id, title, content) VALUES (?, ?, ?, ?)";

    private final JdbcOperations jdbc;
    private final RowMapper<Note> noteRowMapper;

    /**
     * Create a new instance
     * @param jdbc the JdbcOperations instance to use to interact with the database
     */
    public JdbcNotesRepository(JdbcOperations jdbc) {
        this.jdbc = Objects.requireNonNull(jdbc, "jdbc cannot be null");
        this.noteRowMapper = new NoteRowMapper();
    }

    @Override
    public Note get(long id) {
        List<Note> results = jdbc.query(SELECT, stmt -> stmt.setLong(1, id), noteRowMapper);
        return DataAccessUtils.requiredSingleResult(results);
    }

    @Override
    public Note insert(Note note) {
        if (note.getId().isPresent())
            throw new IllegalArgumentException("Cannot insert note with ID already set");

        GeneratedKeyHolder holder = new GeneratedKeyHolder();
        jdbc.update(con -> {
            PreparedStatement stmt = con.prepareStatement(INSERT, new int[] { 1 });

            stmt.setLong(1, note.getOwnerId());
            stmt.setLong(2, note.getCorrelationId());
            stmt.setString(3, note.getTitle());
            stmt.setString(4, note.getContent());

            return stmt;
        }, holder);
        note.setId(holder.getKey().longValue());
        return note;
    }

    @Override
    public Note update(Note note) {
        if (!note.getId().isPresent())
            throw new IllegalArgumentException("Cannot update note without ID");

        int updated = jdbc.update(UPDATE, stmt -> {
            stmt.setLong(1, note.getOwnerId());
            stmt.setLong(2, note.getCorrelationId());
            stmt.setString(3, note.getTitle());
            stmt.setString(4, note.getContent());
            stmt.setLong(5, note.getId().getAsLong());
        });
        if (updated != 1)
            throw new JdbcUpdateAffectedIncorrectNumberOfRowsException(UPDATE, 1, updated);
        return note;
    }

    @Override
    public void delete(long id) {
        jdbc.update(DELETE, stmt -> stmt.setLong(1, id));
    }

    /**
     * Delete all notes
     */
    public void deleteAll() {
        jdbc.update(DELETE_ALL);
    }
}
