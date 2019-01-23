package net.plshark.notes.repo.jdbc;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.inject.Named;
import javax.inject.Singleton;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.JdbcUpdateAffectedIncorrectNumberOfRowsException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import net.plshark.jdbc.SafePreparedStatementCreator;
import net.plshark.notes.Note;

/**
 * Repository that saves notes in and retrieves notes from a DB using JDBC
 */
@Named
@Singleton
public class SyncJdbcNotesRepository {

    private static final String SELECT = "SELECT * FROM notes WHERE id = ?";
    private static final String DELETE = "DELETE FROM notes WHERE id = ?";
    private static final String DELETE_ALL = "DELETE FROM notes";
    private static final String UPDATE = "UPDATE notes SET correlation_id = ?, title = ?, content = ? WHERE id = ?";
    private static final String INSERT = "INSERT INTO notes (correlation_id, title, content) VALUES (?, ?, ?)";

    private final JdbcOperations jdbc;
    private final NoteRowMapper noteRowMapper;

    /**
     * Create a new instance
     * @param jdbc the JdbcOperations instance to use to interact with the database
     */
    public SyncJdbcNotesRepository(JdbcOperations jdbc) {
        this.jdbc = Objects.requireNonNull(jdbc, "jdbc cannot be null");
        this.noteRowMapper = new NoteRowMapper();
    }

    /**
     * Get a note by ID
     * @param id the note ID
     * @return the matching note
     * @throws EmptyResultDataAccessException if there is no matching note
     * @throws DataAccessException if the query fails
     */
    public Optional<Note> get(long id) {
        List<Note> results = jdbc.query(SELECT, stmt -> stmt.setLong(1, id), noteRowMapper);
        return Optional.ofNullable(DataAccessUtils.singleResult(results));
    }

    /**
     * Insert a new note
     * @param note the note to insert
     * @return the inserted note
     * @throws DataAccessException if the insert fails
     */
    public Note insert(Note note) {
        if (note.getId().isPresent())
            throw new IllegalArgumentException("Cannot insert note with ID already set");

        GeneratedKeyHolder holder = new GeneratedKeyHolder();
        jdbc.update(new SafePreparedStatementCreator(
                con -> con.prepareStatement(INSERT, new String[] { "id" }),
                stmt -> {
                    stmt.setLong(1, note.getCorrelationId());
                    stmt.setString(2, note.getTitle());
                    stmt.setString(3, note.getContent());
                }),
            holder);
        Long id = Optional.ofNullable(holder.getKey())
                .map(Number::longValue)
                .orElseThrow(() -> new JdbcUpdateAffectedIncorrectNumberOfRowsException(INSERT, 1, 0));
        return new Note(id, note.getCorrelationId(), note.getTitle(), note.getContent());
    }

    /**
     * Update an existing note
     * @param note the note to update
     * @return the updated note
     * @throws DataAccessException if the update fails
     */
    public Note update(Note note) {
        if (!note.getId().isPresent())
            throw new IllegalArgumentException("Cannot update note without ID");

        int updated = jdbc.update(UPDATE, stmt -> {
            stmt.setLong(1, note.getCorrelationId());
            stmt.setString(2, note.getTitle());
            stmt.setString(3, note.getContent());
            stmt.setLong(4, note.getId().get());
        });
        if (updated != 1)
            throw new JdbcUpdateAffectedIncorrectNumberOfRowsException(UPDATE, 1, updated);
        return note;
    }

    /**
     * Delete a note by ID
     * @param id the ID of the note to delete
     * @throws DataAccessException if the delete fails
     */
    public void delete(long id) {
        jdbc.update(DELETE, stmt -> stmt.setLong(1, id));
    }

    /**
     * Delete all notes
     * @throws DataAccessException if the delete fails
     */
    public void deleteAll() {
        jdbc.update(DELETE_ALL);
    }
}
