package net.plshark.notes.repo;

import java.util.Optional;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;

import net.plshark.notes.Note;

/**
 * Repository for saving, retrieving, and deleting notes
 */
public interface SyncNotesRepository {

    /**
     * Get a note by ID
     * @param id the note ID
     * @return the matching note
     * @throws EmptyResultDataAccessException if there is no matching note
     * @throws DataAccessException if the query fails
     */
    Optional<Note> get(long id);

    /**
     * Insert a new note
     * @param note the note to insert
     * @return the inserted note
     * @throws DataAccessException if the insert fails
     */
    Note insert(Note note);

    /**
     * Update an existing note
     * @param note the note to update
     * @return the updated note
     * @throws DataAccessException if the update fails
     */
    Note update(Note note);

    /**
     * Delete a note by ID
     * @param id the ID of the note to delete
     * @throws DataAccessException if the delete fails
     */
    void delete(long id);
}
